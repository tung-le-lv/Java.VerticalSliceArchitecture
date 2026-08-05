package com.openmind.payment.features.processpayment;

import com.openmind.payment.shared.mediator.Request;

import java.math.BigDecimal;

public record ProcessPaymentCommand(String orderId, String customerId, BigDecimal amount) implements Request<ProcessPaymentResult> {
}
