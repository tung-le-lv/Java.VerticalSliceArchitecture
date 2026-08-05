package com.openmind.payment.features.processpayment;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.openmind.payment.shared.mediator.Mediator;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SQS-driven background consumer — the real "endpoint" of this service.
 * Long-polls PAYMENT_ORDER_QUEUE_URL for OrderPlaced events published by
 * Order.Api and dispatches a ProcessPaymentCommand for each. Unlike
 * HandlePaymentProcessedConsumer in Order.Api, the message is deleted
 * regardless of whether the payment was accepted or declined — only an
 * exception during processing leaves it in the queue for SQS to redeliver.
 */
@Component
public class ProcessPaymentConsumer
{
    private static final Logger log = LoggerFactory.getLogger(ProcessPaymentConsumer.class);

    private final SqsClient sqsClient;
    private final Mediator mediator;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    private volatile boolean running = false;
    private ExecutorService executor;

    public ProcessPaymentConsumer(SqsClient sqsClient, Mediator mediator, ObjectMapper objectMapper,
            @Value("${PAYMENT_ORDER_QUEUE_URL:}") String queueUrl)
    {
        this.sqsClient = sqsClient;
        this.mediator = mediator;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start()
    {
        if (queueUrl == null || queueUrl.isBlank())
        {
            log.warn("PAYMENT_ORDER_QUEUE_URL is not configured; ProcessPaymentConsumer will not run.");
            return;
        }

        running = true;
        // Non-daemon: this service has no web server
        // (spring.main.web-application-type=none),
        // so this thread is the only thing keeping the JVM alive between poll cycles.
        executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "process-payment-consumer"));
        executor.submit(this::pollLoop);
    }

    @PreDestroy
    public void stop()
    {
        running = false;
        if (executor != null)
        {
            executor.shutdownNow();
        }
    }

    private void pollLoop()
    {
        while (running)
        {
            ReceiveMessageResponse response;
            try
            {
                response = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl)
                        .maxNumberOfMessages(10).waitTimeSeconds(20).build());
            } catch (Exception e)
            {
                if (!running)
                {
                    break;
                }
                log.error("Failed to poll SQS queue {}", queueUrl, e);
                continue;
            }

            for (Message message : response.messages())
            {
                processMessage(message);
            }
        }
    }

    private void processMessage(Message message)
    {
        try
        {
            JsonNode notification = objectMapper.readTree(message.body());
            JsonNode envelope = objectMapper.readTree(notification.get("Message").asString());
            String eventType = envelope.get("eventType").asString();

            if (!"OrderPlaced".equals(eventType))
            {
                deleteMessage(message);
                return;
            }

            OrderPlacedData data = objectMapper.treeToValue(envelope.get("data"), OrderPlacedData.class);

            log.info("Processing payment for order {}, customer {}, amount {}", data.orderId(), data.customerId(),
                    data.totalAmount());

            ProcessPaymentResult result = mediator
                    .send(new ProcessPaymentCommand(data.orderId(), data.customerId(), data.totalAmount()));

            if (!result.success())
            {
                log.warn("Payment failed for order {}: {}", data.orderId(), result.message());
            }

            deleteMessage(message);
        } catch (Exception e)
        {
            log.error("Failed to process SQS message {}", message.messageId(), e);
        }
    }

    private void deleteMessage(Message message)
    {
        sqsClient.deleteMessage(
                DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build());
    }
}
