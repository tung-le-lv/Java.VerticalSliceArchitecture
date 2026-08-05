package com.openmind.order.domain.valueobjects;

import com.openmind.order.domain.DomainException;

import java.math.BigDecimal;

public record OrderItem(String productId, String productName, int quantity, Money unitPrice) {

    public Money subtotal()
    {
        return Money.fromDecimal(unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity)));
    }

    public static OrderItem create(String productId, String productName, int quantity, BigDecimal unitPrice)
    {
        if (productId == null || productId.isBlank())
        {
            throw new DomainException("Product ID is required.");
        }
        if (productName == null || productName.isBlank())
        {
            throw new DomainException("Product name is required.");
        }
        if (quantity <= 0)
        {
            throw new DomainException("Quantity must be greater than zero.");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new DomainException("Unit price cannot be negative.");
        }
        return new OrderItem(productId, productName, quantity, Money.fromDecimal(unitPrice));
    }

    public static OrderItem reconstitute(String productId, String productName, int quantity, BigDecimal unitPrice)
    {
        return new OrderItem(productId, productName, quantity, Money.fromDecimal(unitPrice));
    }

    public OrderItem increaseQuantity(int amount)
    {
        if (amount <= 0)
        {
            throw new DomainException("Amount must be greater than zero.");
        }
        return new OrderItem(productId, productName, quantity + amount, unitPrice);
    }

    public OrderItem decreaseQuantity(int amount)
    {
        if (amount <= 0)
        {
            throw new DomainException("Amount must be greater than zero.");
        }
        if (quantity - amount < 1)
        {
            throw new DomainException("Quantity cannot be less than 1.");
        }
        return new OrderItem(productId, productName, quantity - amount, unitPrice);
    }

    public OrderItem updateQuantity(int newQuantity)
    {
        if (newQuantity <= 0)
        {
            throw new DomainException("Quantity must be greater than zero.");
        }
        return new OrderItem(productId, productName, newQuantity, unitPrice);
    }
}
