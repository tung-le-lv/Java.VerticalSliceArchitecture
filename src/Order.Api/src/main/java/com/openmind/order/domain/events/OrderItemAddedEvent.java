package com.openmind.order.domain.events;

public final class OrderItemAddedEvent extends DomainEventBase {
    private final String orderId;
    private final String productId;
    private final int quantity;

    public OrderItemAddedEvent(String orderId, String productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String getEventType() {
        return "OrderItemAdded";
    }

    @Override
    public String getMessageGroupId() {
        return orderId;
    }
}
