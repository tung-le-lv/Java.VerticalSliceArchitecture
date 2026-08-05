package com.openmind.order.features.cancelorder;

import com.openmind.order.shared.mediator.Request;

public record CancelOrderCommand(String orderId) implements Request<CancelOrderResult> {
}
