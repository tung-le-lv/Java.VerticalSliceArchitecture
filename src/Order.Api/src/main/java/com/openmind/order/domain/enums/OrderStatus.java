package com.openmind.order.domain.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Constant names are kept in the original PascalCase (not Java's
 * UPPER_SNAKE_CASE) on purpose: the DynamoDB Local seed data hardcoded in
 * docker-compose.yaml stores these exact strings, and changing casing here
 * would break reads of that pre-seeded data.
 */
public enum OrderStatus
{
    Pending, Confirmed, Processing, Shipped, Delivered, Cancelled, PaymentConfirmed;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static
    {
        VALID_TRANSITIONS.put(Pending, EnumSet.of(Confirmed, Cancelled, PaymentConfirmed));
        VALID_TRANSITIONS.put(PaymentConfirmed, EnumSet.of(Processing, Cancelled));
        VALID_TRANSITIONS.put(Confirmed, EnumSet.of(Processing, Cancelled, PaymentConfirmed));
        VALID_TRANSITIONS.put(Processing, EnumSet.of(Shipped, Cancelled));
        VALID_TRANSITIONS.put(Shipped, EnumSet.of(Delivered));
        VALID_TRANSITIONS.put(Delivered, EnumSet.noneOf(OrderStatus.class));
        VALID_TRANSITIONS.put(Cancelled, EnumSet.noneOf(OrderStatus.class));
    }

    public boolean canTransitionTo(OrderStatus newStatus)
    {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(newStatus);
    }

    public static OrderStatus parseIgnoreCase(String value)
    {
        for (OrderStatus status : values())
        {
            if (status.name().equalsIgnoreCase(value))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching OrderStatus for '" + value + "'.");
    }
}
