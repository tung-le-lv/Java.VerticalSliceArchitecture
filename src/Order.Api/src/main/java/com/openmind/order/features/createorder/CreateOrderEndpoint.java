package com.openmind.order.features.createorder;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CreateOrderEndpoint
{

    private final Mediator mediator;

    public CreateOrderEndpoint(Mediator mediator)
    {
        this.mediator = mediator;
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<?>> handle(@RequestBody CreateOrderCommand command)
    {
        CreateOrderResult result = mediator.send(command);

        if (!result.success())
        {
            return ResponseEntity.badRequest().body(ApiResponse.errorResponse(
                    result.message() != null ? result.message() : "Failed to create order.", result.errors()));
        }

        return ResponseEntity.created(URI.create("/orders/" + result.orderId()))
                .body(ApiResponse.successResponse(new OrderIdResponse(result.orderId()), result.message()));
    }

    public record OrderIdResponse(String orderId) {
    }
}
