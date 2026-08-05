package com.openmind.payment.features.processpayment;

public record ProcessPaymentResult(boolean success, String paymentId, String message) {
}
