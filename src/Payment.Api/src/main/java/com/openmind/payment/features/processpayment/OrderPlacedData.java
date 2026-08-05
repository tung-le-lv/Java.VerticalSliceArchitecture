package com.openmind.payment.features.processpayment;

import java.math.BigDecimal;

/**
 * Shape of the "data" payload inside the OrderPlaced event envelope published
 * by Order.Api and consumed here.
 */
public record OrderPlacedData(String orderId, String customerId, BigDecimal totalAmount) {
}
