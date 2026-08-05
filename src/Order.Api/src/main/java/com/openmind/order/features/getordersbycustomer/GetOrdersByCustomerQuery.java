package com.openmind.order.features.getordersbycustomer;

import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Request;

import java.util.List;

public record GetOrdersByCustomerQuery(String customerId) implements Request<List<OrderDto>> {
}
