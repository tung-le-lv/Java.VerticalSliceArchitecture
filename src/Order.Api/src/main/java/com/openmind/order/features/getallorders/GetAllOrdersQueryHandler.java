package com.openmind.order.features.getallorders;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.application.dto.OrderMapper;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllOrdersQueryHandler implements RequestHandler<GetAllOrdersQuery, List<OrderDto>>
{

    private final OrderRepository orderRepository;

    public GetAllOrdersQueryHandler(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> handle(GetAllOrdersQuery request)
    {
        return orderRepository.getAll().stream().map(OrderMapper::toDto).toList();
    }
}
