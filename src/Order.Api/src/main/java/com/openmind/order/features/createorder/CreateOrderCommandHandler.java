package com.openmind.order.features.createorder;

import com.openmind.order.domain.DomainException;
import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.events.DomainEvent;
import com.openmind.order.domain.repositories.OrderRepository;
import com.openmind.order.shared.application.interfaces.EventBus;
import com.openmind.order.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateOrderCommandHandler implements RequestHandler<CreateOrderCommand, CreateOrderResult> {

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    public CreateOrderCommandHandler(OrderRepository orderRepository, EventBus eventBus) {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @Override
    public CreateOrderResult handle(CreateOrderCommand request) {
        try {
            OrderAggregate order = OrderAggregate.create(request.customerId());

            for (CreateOrderItemDto item : request.items()) {
                order.addItem(item.productId(), item.productName(), item.quantity(), item.unitPrice());
            }

            orderRepository.add(order);

            for (DomainEvent domainEvent : order.getDomainEvents()) {
                eventBus.publish(domainEvent);
            }
            order.clearDomainEvents();

            return new CreateOrderResult(true, order.getId(), "Order created successfully.", null);
        } catch (DomainException ex) {
            return new CreateOrderResult(false, null, "Domain validation failed.", List.of(ex.getMessage()));
        } catch (Exception ex) {
            return new CreateOrderResult(false, null, "An error occurred while creating the order.", List.of(String.valueOf(ex.getMessage())));
        }
    }
}
