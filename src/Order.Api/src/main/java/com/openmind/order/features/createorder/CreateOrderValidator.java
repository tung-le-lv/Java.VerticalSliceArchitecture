package com.openmind.order.features.createorder;

import com.openmind.order.shared.mediator.RequestValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateOrderValidator implements RequestValidator<CreateOrderCommand>
{

    @Override
    public List<String> validate(CreateOrderCommand request)
    {
        List<String> errors = new ArrayList<>();

        if (isBlank(request.customerId()))
        {
            errors.add("Customer ID is required.");
        }
        if (request.customerId() != null && request.customerId().length() > 100)
        {
            errors.add("Customer ID must not exceed 100 characters.");
        }
        if (request.items() == null || request.items().isEmpty())
        {
            errors.add("At least one order item is required.");
        }

        if (request.items() != null)
        {
            CreateOrderItemDtoValidator itemValidator = new CreateOrderItemDtoValidator();
            for (CreateOrderItemDto item : request.items())
            {
                errors.addAll(itemValidator.validate(item));
            }
        }

        return errors;
    }

    private static boolean isBlank(String value)
    {
        return value == null || value.isBlank();
    }
}
