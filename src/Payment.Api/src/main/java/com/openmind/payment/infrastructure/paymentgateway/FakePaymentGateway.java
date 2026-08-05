package com.openmind.payment.infrastructure.paymentgateway;

import com.openmind.payment.domain.entities.PaymentAggregate;
import com.openmind.payment.shared.application.interfaces.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentGateway implements PaymentGateway {
    @Override
    public boolean charge(PaymentAggregate payment) {
        return true;
    }
}
