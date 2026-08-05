package com.openmind.order.features.deleteorder;

import com.openmind.order.shared.mediator.Request;

public record DeleteOrderCommand(String orderId) implements Request<DeleteOrderResult> {
}
