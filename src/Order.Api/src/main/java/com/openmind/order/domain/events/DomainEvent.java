package com.openmind.order.domain.events;

import java.time.Instant;

public interface DomainEvent {
    String getEventId();

    Instant getOccurredAt();

    String getEventType();

    String getMessageGroupId();
}
