package com.openmind.order.features.addorderitem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.openmind.order.shared.mediator.RequestValidator;

/**
 * Field-level rules (blank checks, max lengths, positivity) live as Jakarta
 * Bean Validation annotations on {@link AddOrderItemCommand} and run
 * automatically via the mediator. This validator only covers the one rule that
 * spans multiple fields and can't be expressed as a single-field constraint
 * annotation.
 */
@Component
public class AddOrderItemValidator implements RequestValidator<AddOrderItemCommand>
{

    private static final BigDecimal MAX_LINE_TOTAL = new BigDecimal("10000.00");

    @Override
    public List<String> validate(AddOrderItemCommand request)
    {
        List<String> errors = new ArrayList<>();

        if (request.quantity() > 0)
        {
            BigDecimal lineTotal = request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));

            if (lineTotal.compareTo(MAX_LINE_TOTAL) > 0)
            {
                errors.add("Line total (quantity x unit price) must not exceed " + MAX_LINE_TOTAL + ".");
            }
        }

        return errors;
    }
}
