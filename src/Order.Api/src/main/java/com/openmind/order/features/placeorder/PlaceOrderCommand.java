package com.openmind.order.features.placeorder;

import com.openmind.order.shared.mediator.Request;

public record PlaceOrderCommand(String orderId) implements Request<PlaceOrderResult> {
}
