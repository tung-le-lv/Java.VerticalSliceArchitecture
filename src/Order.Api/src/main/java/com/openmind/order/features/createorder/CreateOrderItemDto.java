package com.openmind.order.features.createorder;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateOrderItemDto(
                @NotBlank(message = "Product ID is required.") String productId,

                @NotBlank(message = "Product name is required.") @Size(max = 200, message = "Product name must not exceed 200 characters.") String productName,

                @Positive(message = "Quantity must be greater than zero.") int quantity,

                @NotNull(message = "Unit price cannot be negative.") @PositiveOrZero(message = "Unit price cannot be negative.") BigDecimal unitPrice) {
}
