package com.openmind.order.shared.application.dto;

import java.math.BigDecimal;

public record OrderItemDto(String productId, String productName, int quantity, BigDecimal unitPrice,
        BigDecimal subtotal) {
}
