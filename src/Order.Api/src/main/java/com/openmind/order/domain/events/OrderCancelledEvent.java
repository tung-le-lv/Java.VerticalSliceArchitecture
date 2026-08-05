package com.openmind.order.domain.events;

import com.openmind.order.domain.enums.OrderStatus;

public final class OrderCancelledEvent extends DomainEventBase {
    private final String orderId;
    private final OrderStatus previousStatus;

    public OrderCancelledEvent(String orderId, OrderStatus previousStatus) {
        this.orderId = orderId;
        this.previousStatus = previousStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getPreviousStatus() {
        return previousStatus;
    }

    @Override
    public String getEventType() {
        return "OrderCancelled";
    }

    @Override
    public String getMessageGroupId() {
        return orderId;
    }
}
