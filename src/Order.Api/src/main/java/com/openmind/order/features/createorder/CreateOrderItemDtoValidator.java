package com.openmind.order.features.createorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Not a mediator-dispatched validator (there is no Request&lt;T&gt; of type
 * CreateOrderItemDto) — invoked directly by CreateOrderValidator for each item,
 * mirroring FluentValidation's RuleForEach(...).SetValidator(new
 * CreateOrderItemDtoValidator()) composition.
 */
class CreateOrderItemDtoValidator
{

    List<String> validate(CreateOrderItemDto item)
    {
        List<String> errors = new ArrayList<>();

        if (isBlank(item.productId()))
        {
            errors.add("Product ID is required.");
        }
        if (isBlank(item.productName()))
        {
            errors.add("Product name is required.");
        }
        if (item.productName() != null && item.productName().length() > 200)
        {
            errors.add("Product name must not exceed 200 characters.");
        }
        if (item.quantity() <= 0)
        {
            errors.add("Quantity must be greater than zero.");
        }
        if (item.unitPrice() == null || item.unitPrice().compareTo(BigDecimal.ZERO) < 0)
        {
            errors.add("Unit price cannot be negative.");
        }

        return errors;
    }

    private static boolean isBlank(String value)
    {
        return value == null || value.isBlank();
    }
}
