package com.openmind.order.domain.repositories;

import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository
{
    Optional<OrderAggregate> getById(String orderId);

    List<OrderAggregate> getByCustomerId(String customerId);

    List<OrderAggregate> getByCustomerIdAndStatus(String customerId, OrderStatus status);

    List<OrderAggregate> getByDate(LocalDate date);

    List<OrderAggregate> getAll();

    OrderAggregate add(OrderAggregate order);

    OrderAggregate update(OrderAggregate order);

    void delete(String orderId);
}
