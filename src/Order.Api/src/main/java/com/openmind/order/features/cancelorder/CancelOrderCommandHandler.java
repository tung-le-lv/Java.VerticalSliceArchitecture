package com.openmind.order.features.cancelorder;

import com.openmind.order.domain.DomainException;
import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.events.DomainEvent;
import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.interfaces.EventBus;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CancelOrderCommandHandler implements RequestHandler<CancelOrderCommand, CancelOrderResult>
{

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    public CancelOrderCommandHandler(OrderRepository orderRepository, EventBus eventBus)
    {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @Override
    public CancelOrderResult handle(CancelOrderCommand request)
    {
        try
        {
            Optional<OrderAggregate> maybeOrder = orderRepository.getById(request.orderId());
            if (maybeOrder.isEmpty())
            {
                return new CancelOrderResult(false, "Order with ID '" + request.orderId() + "' not found.", null);
            }

            OrderAggregate order = maybeOrder.get();
            order.cancel();
            orderRepository.update(order);

            for (DomainEvent domainEvent : order.getDomainEvents())
            {
                eventBus.publish(domainEvent);
            }
            order.clearDomainEvents();

            return new CancelOrderResult(true, "Order cancelled successfully.", null);
        }
        catch (DomainException ex)
        {
            return new CancelOrderResult(false, "Cancellation failed.", List.of(ex.getMessage()));
        }
        catch (Exception ex)
        {
            return new CancelOrderResult(false, "An error occurred while cancelling the order.",
                    List.of(String.valueOf(ex.getMessage())));
        }
    }
}
