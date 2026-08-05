package com.openmind.order.features.getorder;

import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Request;

public record GetOrderQuery(String orderId) implements Request<OrderDto> {
}
