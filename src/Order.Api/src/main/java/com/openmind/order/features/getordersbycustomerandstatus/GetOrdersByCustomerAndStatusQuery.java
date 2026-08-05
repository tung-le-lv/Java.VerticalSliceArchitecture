package com.openmind.order.features.getordersbycustomerandstatus;

import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Request;

import java.util.List;

public record GetOrdersByCustomerAndStatusQuery(String customerId,
        OrderStatus status) implements Request<List<OrderDto>> {
}
