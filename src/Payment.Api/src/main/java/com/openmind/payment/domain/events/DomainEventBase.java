package com.openmind.payment.domain.events;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEventBase implements DomainEvent
{
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredAt = Instant.now();

    @Override
    public String getEventId()
    {
        return eventId;
    }

    @Override
    public Instant getOccurredAt()
    {
        return occurredAt;
    }

    @Override
    public String getMessageGroupId()
    {
        return getEventType();
    }
}
