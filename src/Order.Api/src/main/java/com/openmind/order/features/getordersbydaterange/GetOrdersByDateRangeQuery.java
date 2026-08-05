package com.openmind.order.features.getordersbydaterange;

import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Request;

import java.time.LocalDate;
import java.util.List;

public record GetOrdersByDateRangeQuery(LocalDate date) implements Request<List<OrderDto>> {
}
