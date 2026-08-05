package com.openmind.order.features.getordersbycustomer;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.application.dto.OrderMapper;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOrdersByCustomerQueryHandler implements RequestHandler<GetOrdersByCustomerQuery, List<OrderDto>> {

    private final OrderRepository orderRepository;

    public GetOrdersByCustomerQueryHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> handle(GetOrdersByCustomerQuery request) {
        return orderRepository.getByCustomerId(request.customerId()).stream().map(OrderMapper::toDto).toList();
    }
}
