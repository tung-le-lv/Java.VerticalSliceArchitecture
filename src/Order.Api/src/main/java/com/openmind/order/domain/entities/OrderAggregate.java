package com.openmind.order.domain.entities;

import com.openmind.order.domain.DomainException;
import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.domain.events.DomainEvent;
import com.openmind.order.domain.events.OrderCancelledEvent;
import com.openmind.order.domain.events.OrderCreatedEvent;
import com.openmind.order.domain.events.OrderItemAddedEvent;
import com.openmind.order.domain.events.OrderPlacedEvent;
import com.openmind.order.domain.events.OrderStatusChangedEvent;
import com.openmind.order.domain.valueobjects.Money;
import com.openmind.order.domain.valueobjects.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderAggregate
{
    private final List<OrderItem> items = new ArrayList<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private String id = "";
    private String customerId = "";
    private Money totalAmount = Money.zero();
    private OrderStatus status = OrderStatus.Pending;
    private Instant createdAt;
    private Instant updatedAt;

    private OrderAggregate()
    {
    }

    public static OrderAggregate create(String customerId)
    {
        OrderAggregate order = new OrderAggregate();
        order.id = UUID.randomUUID().toString();
        order.customerId = customerId;
        order.status = OrderStatus.Pending;
        order.createdAt = Instant.now();
        order.updatedAt = order.createdAt;
        order.addDomainEvent(new OrderCreatedEvent(order.id, customerId));
        return order;
    }

    public static OrderAggregate reconstitute(String id, String customerId, List<OrderItem> items,
            BigDecimal totalAmount, OrderStatus status, Instant createdAt, Instant updatedAt)
    {
        OrderAggregate order = new OrderAggregate();
        order.id = id;
        order.customerId = customerId;
        order.totalAmount = Money.fromDecimal(totalAmount);
        order.status = status;
        order.createdAt = createdAt;
        order.updatedAt = updatedAt;
        order.items.addAll(items);
        return order;
    }

    public void place()
    {
        if (status != OrderStatus.Pending)
        {
            throw new DomainException("Only a pending order can be placed.");
        }
        if (items.isEmpty())
        {
            throw new DomainException("Cannot place an empty order.");
        }
        status = OrderStatus.Confirmed;
        updateTimestamp();
        addDomainEvent(new OrderPlacedEvent(id, customerId, totalAmount.getAmount()));
    }

    public void addItem(String productId, String productName, int quantity, BigDecimal unitPrice)
    {
        if (status != OrderStatus.Pending)
        {
            throw new DomainException("Cannot add items to a non-pending order.");
        }

        int index = -1;
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).productId().equals(productId))
            {
                index = i;
                break;
            }
        }

        if (index >= 0)
        {
            items.set(index, items.get(index).increaseQuantity(quantity));
        }
        else
        {
            items.add(OrderItem.create(productId, productName, quantity, unitPrice));
        }

        recalculateTotal();
        updateTimestamp();
        addDomainEvent(new OrderItemAddedEvent(id, productId, quantity));
    }

    public void removeItem(String productId)
    {
        if (status != OrderStatus.Pending)
        {
            throw new DomainException("Cannot remove items from a non-pending order.");
        }

        OrderItem item = items.stream().filter(i -> i.productId().equals(productId)).findFirst().orElse(null);
        if (item == null)
        {
            throw new DomainException("Item with product ID '" + productId + "' not found.");
        }

        items.remove(item);
        recalculateTotal();
        updateTimestamp();
    }

    public void updateStatus(OrderStatus newStatus)
    {
        if (!status.canTransitionTo(newStatus))
        {
            throw new DomainException("Invalid status transition from " + status + " to " + newStatus + ".");
        }

        OrderStatus oldStatus = status;
        status = newStatus;
        updateTimestamp();
        addDomainEvent(new OrderStatusChangedEvent(id, oldStatus, newStatus));
    }

    public void cancel()
    {
        if (!status.canTransitionTo(OrderStatus.Cancelled))
        {
            throw new DomainException("Cannot cancel an order that has been shipped or delivered.");
        }

        OrderStatus oldStatus = status;
        status = OrderStatus.Cancelled;
        updateTimestamp();
        addDomainEvent(new OrderCancelledEvent(id, oldStatus));
    }

    public void clearDomainEvents()
    {
        domainEvents.clear();
    }

    private void recalculateTotal()
    {
        BigDecimal total = items.stream().map(item -> item.subtotal().getAmount()).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        totalAmount = Money.fromDecimal(total);
    }

    private void updateTimestamp()
    {
        updatedAt = Instant.now();
    }

    private void addDomainEvent(DomainEvent domainEvent)
    {
        domainEvents.add(domainEvent);
    }

    public String getId()
    {
        return id;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public List<OrderItem> getItems()
    {
        return Collections.unmodifiableList(items);
    }

    public Money getTotalAmount()
    {
        return totalAmount;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public Instant getCreatedAt()
    {
        return createdAt;
    }

    public Instant getUpdatedAt()
    {
        return updatedAt;
    }

    public List<DomainEvent> getDomainEvents()
    {
        return Collections.unmodifiableList(domainEvents);
    }
}
