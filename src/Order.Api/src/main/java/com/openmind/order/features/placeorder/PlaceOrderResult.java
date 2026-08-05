package com.openmind.order.features.placeorder;

import com.openmind.order.shared.OperationResult;

import java.util.List;

public record PlaceOrderResult(boolean success, String message, List<String> errors) implements OperationResult {
}
