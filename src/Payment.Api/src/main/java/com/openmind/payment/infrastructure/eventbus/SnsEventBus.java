package com.openmind.payment.infrastructure.eventbus;

import tools.jackson.databind.ObjectMapper;
import com.openmind.payment.domain.events.DomainEvent;
import com.openmind.payment.shared.application.interfaces.EventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SnsEventBus implements EventBus
{

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;
    private final String topicArn;

    public SnsEventBus(SnsClient snsClient, ObjectMapper objectMapper,
            @Value("${PAYMENT_EVENTS_TOPIC_ARN:}") String topicArn)
    {
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
        this.topicArn = topicArn;
    }

    @Override
    public void publish(DomainEvent domainEvent)
    {
        if (topicArn == null || topicArn.isBlank())
        {
            return;
        }

        try
        {
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("eventId", domainEvent.getEventId());
            message.put("eventType", domainEvent.getEventType());
            message.put("occurredAt", domainEvent.getOccurredAt());
            message.put("data", domainEvent);

            snsClient
                    .publish(
                            PublishRequest.builder().topicArn(topicArn)
                                    .message(objectMapper.writeValueAsString(message))
                                    .messageGroupId(domainEvent.getMessageGroupId())
                                    .messageAttributes(Map.of("EventType", MessageAttributeValue.builder()
                                            .dataType("String").stringValue(domainEvent.getEventType()).build()))
                                    .build());
        } catch (Exception e)
        {
            throw new IllegalStateException("Failed to publish domain event " + domainEvent.getEventType(), e);
        }
    }
}
