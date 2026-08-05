package com.openmind.order.shared.application.dto;

import com.openmind.order.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(
        String id,
        String customerId,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        String currency,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
