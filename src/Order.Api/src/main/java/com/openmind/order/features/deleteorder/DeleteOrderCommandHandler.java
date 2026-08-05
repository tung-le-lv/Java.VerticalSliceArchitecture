package com.openmind.order.features.deleteorder;

import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

@Service
public class DeleteOrderCommandHandler implements RequestHandler<DeleteOrderCommand, DeleteOrderResult>
{

    private final OrderRepository orderRepository;

    public DeleteOrderCommandHandler(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public DeleteOrderResult handle(DeleteOrderCommand request)
    {
        try
        {
            orderRepository.delete(request.orderId());
            return new DeleteOrderResult(true, "Order deleted successfully.");
        } catch (Exception ex)
        {
            return new DeleteOrderResult(false, "An error occurred while deleting the order: " + ex.getMessage());
        }
    }
}
