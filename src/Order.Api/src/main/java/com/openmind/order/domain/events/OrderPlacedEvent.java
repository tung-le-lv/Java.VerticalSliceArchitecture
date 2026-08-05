package com.openmind.order.domain.events;

import java.math.BigDecimal;

public final class OrderPlacedEvent extends DomainEventBase
{
    private final String orderId;
    private final String customerId;
    private final BigDecimal totalAmount;

    public OrderPlacedEvent(String orderId, String customerId, BigDecimal totalAmount)
    {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    @Override
    public String getEventType()
    {
        return "OrderPlaced";
    }

    @Override
    public String getMessageGroupId()
    {
        return orderId;
    }
}
