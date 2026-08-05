package com.openmind.order.features.createorder;

import java.math.BigDecimal;

public record CreateOrderItemDto(String productId, String productName, int quantity, BigDecimal unitPrice) {
}
