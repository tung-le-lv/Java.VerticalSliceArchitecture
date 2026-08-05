package com.openmind.order.shared.application.dto;

import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.valueobjects.OrderItem;

public final class OrderMapper {
    private OrderMapper() {
    }

    public static OrderDto toDto(OrderAggregate order) {
        return new OrderDto(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream().map(OrderMapper::toItemDto).toList(),
                order.getTotalAmount().getAmount(),
                order.getTotalAmount().getCurrency(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private static OrderItemDto toItemDto(OrderItem item) {
        return new OrderItemDto(
                item.productId(),
                item.productName(),
                item.quantity(),
                item.unitPrice().getAmount(),
                item.subtotal().getAmount()
        );
    }
}
