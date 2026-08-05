package com.openmind.payment.shared.application.interfaces;

import com.openmind.payment.domain.events.DomainEvent;

public interface EventBus {
    void publish(DomainEvent domainEvent);
}
