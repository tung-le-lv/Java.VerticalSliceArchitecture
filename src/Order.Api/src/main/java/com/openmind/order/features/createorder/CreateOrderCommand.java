package com.openmind.order.features.createorder;

import com.openmind.order.shared.mediator.Request;

import java.util.List;

public record CreateOrderCommand(String customerId,
        List<CreateOrderItemDto> items) implements Request<CreateOrderResult> {
}
