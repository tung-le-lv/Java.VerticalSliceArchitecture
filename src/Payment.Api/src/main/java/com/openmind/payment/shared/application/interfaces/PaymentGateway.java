package com.openmind.payment.shared.application.interfaces;

import com.openmind.payment.domain.entities.PaymentAggregate;

public interface PaymentGateway {
    boolean charge(PaymentAggregate payment);
}
