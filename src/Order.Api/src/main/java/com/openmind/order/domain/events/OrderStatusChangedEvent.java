package com.openmind.order.domain.events;

import com.openmind.order.domain.enums.OrderStatus;

public final class OrderStatusChangedEvent extends DomainEventBase {
    private final String orderId;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(String orderId, OrderStatus oldStatus, OrderStatus newStatus) {
        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    @Override
    public String getEventType() {
        return "OrderStatusChanged";
    }

    @Override
    public String getMessageGroupId() {
        return orderId;
    }
}
