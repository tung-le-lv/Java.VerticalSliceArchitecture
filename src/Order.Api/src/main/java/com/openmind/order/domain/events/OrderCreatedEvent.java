package com.openmind.order.domain.events;

public final class OrderCreatedEvent extends DomainEventBase {
    private final String orderId;
    private final String customerId;

    public OrderCreatedEvent(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getEventType() {
        return "OrderCreated";
    }

    @Override
    public String getMessageGroupId() {
        return orderId;
    }
}
