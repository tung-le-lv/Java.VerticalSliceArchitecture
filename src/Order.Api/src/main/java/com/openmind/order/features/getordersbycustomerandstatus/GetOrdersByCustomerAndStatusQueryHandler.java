package com.openmind.order.features.getordersbycustomerandstatus;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.application.dto.OrderMapper;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOrdersByCustomerAndStatusQueryHandler implements RequestHandler<GetOrdersByCustomerAndStatusQuery, List<OrderDto>> {

    private final OrderRepository orderRepository;

    public GetOrdersByCustomerAndStatusQueryHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> handle(GetOrdersByCustomerAndStatusQuery request) {
        return orderRepository.getByCustomerIdAndStatus(request.customerId(), request.status()).stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}
