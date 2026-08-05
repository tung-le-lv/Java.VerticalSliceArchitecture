package com.openmind.order.features.addorderitem;

import com.openmind.order.shared.OperationResult;

import java.util.List;

public record AddOrderItemResult(boolean success, String message, List<String> errors) implements OperationResult {
}
