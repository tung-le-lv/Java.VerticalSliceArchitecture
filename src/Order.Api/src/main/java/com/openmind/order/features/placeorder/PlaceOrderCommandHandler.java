package com.openmind.order.features.placeorder;

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
public class PlaceOrderCommandHandler implements RequestHandler<PlaceOrderCommand, PlaceOrderResult>
{

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    public PlaceOrderCommandHandler(OrderRepository orderRepository, EventBus eventBus)
    {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @Override
    public PlaceOrderResult handle(PlaceOrderCommand request)
    {
        try
        {
            Optional<OrderAggregate> maybeOrder = orderRepository.getById(request.orderId());
            if (maybeOrder.isEmpty())
            {
                return new PlaceOrderResult(false, "Order '" + request.orderId() + "' not found.", null);
            }

            OrderAggregate order = maybeOrder.get();
            order.place();

            orderRepository.update(order);

            for (DomainEvent domainEvent : order.getDomainEvents())
            {
                eventBus.publish(domainEvent);
            }
            order.clearDomainEvents();

            return new PlaceOrderResult(true, "Order placed successfully.", null);
        } catch (DomainException ex)
        {
            return new PlaceOrderResult(false, ex.getMessage(), null);
        } catch (Exception ex)
        {
            return new PlaceOrderResult(false, "An error occurred while placing the order.",
                    List.of(String.valueOf(ex.getMessage())));
        }
    }
}
