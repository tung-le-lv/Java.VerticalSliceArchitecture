package com.openmind.payment.domain.repositories;

import com.openmind.payment.domain.entities.PaymentAggregate;

import java.util.Optional;

public interface PaymentRepository
{
    Optional<PaymentAggregate> getById(String paymentId);

    Optional<PaymentAggregate> getByOrderId(String orderId);

    PaymentAggregate add(PaymentAggregate payment);
}
