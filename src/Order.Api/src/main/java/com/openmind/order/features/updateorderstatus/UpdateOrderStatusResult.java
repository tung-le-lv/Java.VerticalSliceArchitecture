package com.openmind.order.features.updateorderstatus;

import com.openmind.order.shared.OperationResult;

import java.util.List;

public record UpdateOrderStatusResult(boolean success, String message, List<String> errors) implements OperationResult {
}
