package com.openmind.order.features.addorderitem;

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
public class AddOrderItemCommandHandler implements RequestHandler<AddOrderItemCommand, AddOrderItemResult>
{

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    public AddOrderItemCommandHandler(OrderRepository orderRepository, EventBus eventBus)
    {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @Override
    public AddOrderItemResult handle(AddOrderItemCommand request)
    {
        try
        {
            Optional<OrderAggregate> maybeOrder = orderRepository.getById(request.orderId());
            if (maybeOrder.isEmpty())
            {
                return new AddOrderItemResult(false, "Order with ID '" + request.orderId() + "' not found.", null);
            }

            OrderAggregate order = maybeOrder.get();
            order.addItem(request.productId(), request.productName(), request.quantity(), request.unitPrice());
            orderRepository.update(order);

            for (DomainEvent domainEvent : order.getDomainEvents())
            {
                eventBus.publish(domainEvent);
            }
            order.clearDomainEvents();

            return new AddOrderItemResult(true, "Item added to order successfully.", null);
        } catch (DomainException ex)
        {
            return new AddOrderItemResult(false, "Failed to add item.", List.of(ex.getMessage()));
        } catch (Exception ex)
        {
            return new AddOrderItemResult(false, "An error occurred while adding the item.",
                    List.of(String.valueOf(ex.getMessage())));
        }
    }
}
