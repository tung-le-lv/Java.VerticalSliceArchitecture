package com.openmind.payment.domain.events;

public final class PaymentFailedEvent extends DomainEventBase
{
    private final String paymentId;
    private final String orderId;
    private final String reason;

    public PaymentFailedEvent(String paymentId, String orderId, String reason)
    {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getPaymentId()
    {
        return paymentId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public String getReason()
    {
        return reason;
    }

    @Override
    public String getEventType()
    {
        return "PaymentFailed";
    }

    @Override
    public String getMessageGroupId()
    {
        return orderId;
    }
}
