package com.openmind.order.shared.application.interfaces;

import com.openmind.order.domain.events.DomainEvent;

public interface EventBus
{
    void publish(DomainEvent domainEvent);
}
