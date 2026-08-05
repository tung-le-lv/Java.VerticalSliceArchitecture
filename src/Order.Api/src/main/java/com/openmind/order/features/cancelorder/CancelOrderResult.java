package com.openmind.order.features.cancelorder;

import com.openmind.order.shared.OperationResult;

import java.util.List;

public record CancelOrderResult(boolean success, String message, List<String> errors) implements OperationResult {
}
