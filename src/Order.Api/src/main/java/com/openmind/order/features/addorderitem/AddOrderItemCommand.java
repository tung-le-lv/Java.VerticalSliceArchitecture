package com.openmind.order.features.addorderitem;

import com.openmind.order.shared.mediator.Request;

import java.math.BigDecimal;

public record AddOrderItemCommand(
        String orderId,
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) implements Request<AddOrderItemResult> {
}
