package com.openmind.order.features.handlepaymentprocessed;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.features.updateorderstatus.UpdateOrderStatusCommand;
import com.openmind.order.features.updateorderstatus.UpdateOrderStatusResult;
import com.openmind.order.shared.mediator.Mediator;
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
 * SQS-driven background consumer, the Java equivalent of a .NET
 * BackgroundService: long-polls ORDER_PAYMENT_QUEUE_URL for PaymentProcessed
 * events published by Payment.Api and dispatches an UpdateOrderStatusCommand
 * for each. A message is only deleted once the status update succeeds — on
 * failure or exception it's left in the queue so SQS redelivers it.
 */
@Component
public class HandlePaymentProcessedConsumer
{
    private static final Logger log = LoggerFactory.getLogger(HandlePaymentProcessedConsumer.class);

    private final SqsClient sqsClient;
    private final Mediator mediator;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    private volatile boolean running = false;
    private ExecutorService executor;

    public HandlePaymentProcessedConsumer(SqsClient sqsClient, Mediator mediator, ObjectMapper objectMapper,
            @Value("${ORDER_PAYMENT_QUEUE_URL:}") String queueUrl)
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
            log.warn("ORDER_PAYMENT_QUEUE_URL is not configured; HandlePaymentProcessedConsumer will not run.");
            return;
        }

        running = true;
        executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "handle-payment-processed-consumer"));
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

            if (!"PaymentProcessed".equals(eventType))
            {
                deleteMessage(message);
                return;
            }

            JsonNode data = envelope.get("data");
            String orderId = data.get("orderId").asString();

            log.info("Updating order {} status to PaymentConfirmed", orderId);

            UpdateOrderStatusResult result = mediator
                    .send(new UpdateOrderStatusCommand(orderId, OrderStatus.PaymentConfirmed));

            if (!result.success())
            {
                log.warn("Failed to update order {}: {}", orderId, result.message());
                return;
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
