package com.openmind.order.features.createorder;

import java.util.List;

import com.openmind.order.shared.mediator.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateOrderCommand(
        @NotBlank(message = "Customer ID is required.") @Size(max = 100, message = "Customer ID must not exceed 100 characters.") String customerId,

        @NotEmpty(message = "At least one order item is required.") List<@Valid CreateOrderItemDto> items)
        implements
            Request<CreateOrderResult> {
}
