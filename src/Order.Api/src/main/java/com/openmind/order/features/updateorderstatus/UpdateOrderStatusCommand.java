package com.openmind.order.features.updateorderstatus;

import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.shared.mediator.Request;

public record UpdateOrderStatusCommand(String orderId,
        OrderStatus newStatus) implements Request<UpdateOrderStatusResult> {
}
