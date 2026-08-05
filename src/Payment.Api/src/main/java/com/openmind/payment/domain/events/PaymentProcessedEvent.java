package com.openmind.payment.domain.events;

import java.math.BigDecimal;

public final class PaymentProcessedEvent extends DomainEventBase
{
    private final String paymentId;
    private final String orderId;
    private final String customerId;
    private final BigDecimal amount;

    public PaymentProcessedEvent(String paymentId, String orderId, String customerId, BigDecimal amount)
    {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getPaymentId()
    {
        return paymentId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    @Override
    public String getEventType()
    {
        return "PaymentProcessed";
    }

    @Override
    public String getMessageGroupId()
    {
        return orderId;
    }
}
