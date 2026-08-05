package com.openmind.payment.domain.entities;

import com.openmind.payment.domain.DomainException;
import com.openmind.payment.domain.enums.PaymentStatus;
import com.openmind.payment.domain.events.DomainEvent;
import com.openmind.payment.domain.events.PaymentFailedEvent;
import com.openmind.payment.domain.events.PaymentProcessedEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PaymentAggregate
{
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private String id = "";
    private String orderId = "";
    private String customerId = "";
    private BigDecimal amount;
    private PaymentStatus status = PaymentStatus.Pending;
    private String failureReason;
    private Instant createdAt;
    private Instant processedAt;

    private PaymentAggregate()
    {
    }

    public static PaymentAggregate create(String orderId, String customerId, BigDecimal amount)
    {
        if (orderId == null || orderId.isBlank())
        {
            throw new DomainException("Order ID is required.");
        }
        if (customerId == null || customerId.isBlank())
        {
            throw new DomainException("Customer ID is required.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new DomainException("Payment amount must be greater than zero.");
        }

        PaymentAggregate payment = new PaymentAggregate();
        payment.id = UUID.randomUUID().toString();
        payment.orderId = orderId;
        payment.customerId = customerId;
        payment.amount = amount;
        payment.status = PaymentStatus.Pending;
        payment.createdAt = Instant.now();
        payment.processedAt = payment.createdAt;
        return payment;
    }

    public static PaymentAggregate reconstitute(String id, String orderId, String customerId, BigDecimal amount,
            PaymentStatus status, String failureReason, Instant createdAt, Instant processedAt)
    {
        PaymentAggregate payment = new PaymentAggregate();
        payment.id = id;
        payment.orderId = orderId;
        payment.customerId = customerId;
        payment.amount = amount;
        payment.status = status;
        payment.failureReason = failureReason;
        payment.createdAt = createdAt;
        payment.processedAt = processedAt;
        return payment;
    }

    public void markAsProcessed()
    {
        status = PaymentStatus.Processed;
        processedAt = Instant.now();
        domainEvents.add(new PaymentProcessedEvent(id, orderId, customerId, amount));
    }

    public void markAsFailed(String reason)
    {
        status = PaymentStatus.Failed;
        failureReason = reason;
        processedAt = Instant.now();
        domainEvents.add(new PaymentFailedEvent(id, orderId, reason));
    }

    public void clearDomainEvents()
    {
        domainEvents.clear();
    }

    public String getId()
    {
        return id;
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

    public PaymentStatus getStatus()
    {
        return status;
    }

    public String getFailureReason()
    {
        return failureReason;
    }

    public Instant getCreatedAt()
    {
        return createdAt;
    }

    public Instant getProcessedAt()
    {
        return processedAt;
    }

    public List<DomainEvent> getDomainEvents()
    {
        return Collections.unmodifiableList(domainEvents);
    }
}
