package com.openmind.order.features.addorderitem;

import com.openmind.order.shared.mediator.RequestValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddOrderItemValidator implements RequestValidator<AddOrderItemCommand> {

    @Override
    public List<String> validate(AddOrderItemCommand request) {
        List<String> errors = new ArrayList<>();

        if (isBlank(request.orderId())) {
            errors.add("Order ID is required.");
        }
        if (isBlank(request.productId())) {
            errors.add("Product ID is required.");
        }
        if (isBlank(request.productName())) {
            errors.add("Product name is required.");
        }
        if (request.productName() != null && request.productName().length() > 200) {
            errors.add("Product name must not exceed 200 characters.");
        }
        if (request.quantity() <= 0) {
            errors.add("Quantity must be greater than zero.");
        }
        if (request.unitPrice() == null || request.unitPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Unit price cannot be negative.");
        }

        return errors;
    }

    static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
