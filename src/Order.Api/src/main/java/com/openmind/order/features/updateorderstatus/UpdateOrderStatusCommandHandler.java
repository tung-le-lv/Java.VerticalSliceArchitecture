package com.openmind.order.features.updateorderstatus;

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
public class UpdateOrderStatusCommandHandler implements RequestHandler<UpdateOrderStatusCommand, UpdateOrderStatusResult> {

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    public UpdateOrderStatusCommandHandler(OrderRepository orderRepository, EventBus eventBus) {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @Override
    public UpdateOrderStatusResult handle(UpdateOrderStatusCommand request) {
        try {
            Optional<OrderAggregate> maybeOrder = orderRepository.getById(request.orderId());
            if (maybeOrder.isEmpty()) {
                return new UpdateOrderStatusResult(false, "Order with ID '" + request.orderId() + "' not found.", null);
            }

            OrderAggregate order = maybeOrder.get();
            order.updateStatus(request.newStatus());
            orderRepository.update(order);

            for (DomainEvent domainEvent : order.getDomainEvents()) {
                eventBus.publish(domainEvent);
            }
            order.clearDomainEvents();

            return new UpdateOrderStatusResult(true, "Order status updated successfully.", null);
        } catch (DomainException ex) {
            return new UpdateOrderStatusResult(false, "Status update failed.", List.of(ex.getMessage()));
        } catch (Exception ex) {
            return new UpdateOrderStatusResult(false, "An error occurred while updating the order status.", List.of(String.valueOf(ex.getMessage())));
        }
    }
}
