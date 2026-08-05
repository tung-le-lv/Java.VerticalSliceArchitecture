package com.openmind.order.features.getordersbydaterange;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.application.dto.OrderMapper;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOrdersByDateRangeQueryHandler implements RequestHandler<GetOrdersByDateRangeQuery, List<OrderDto>>
{

    private final OrderRepository orderRepository;

    public GetOrdersByDateRangeQueryHandler(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> handle(GetOrdersByDateRangeQuery request)
    {
        return orderRepository.getByDate(request.date()).stream().map(OrderMapper::toDto).toList();
    }
}
