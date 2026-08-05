package com.openmind.order.features.getallorders;

import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Request;

import java.util.List;

public record GetAllOrdersQuery() implements Request<List<OrderDto>> {
}
