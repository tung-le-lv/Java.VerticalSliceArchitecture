package com.openmind.order.features.createorder;

import java.util.List;

public record CreateOrderResult(boolean success, String orderId, String message, List<String> errors) {
}
