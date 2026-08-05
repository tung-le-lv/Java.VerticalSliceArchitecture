package com.openmind.order.features.getorder;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.application.dto.OrderMapper;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

@Service
public class GetOrderQueryHandler implements RequestHandler<GetOrderQuery, OrderDto>
{

    private final OrderRepository orderRepository;

    public GetOrderQueryHandler(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto handle(GetOrderQuery request)
    {
        return orderRepository.getById(request.orderId()).map(OrderMapper::toDto).orElse(null);
    }
}
