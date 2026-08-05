package com.openmind.order.features.getorder;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetOrderEndpoint
{

    private final Mediator mediator;

    public GetOrderEndpoint(Mediator mediator)
    {
        this.mediator = mediator;
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> handle(@PathVariable("id") String id)
    {
        OrderDto result = mediator.send(new GetOrderQuery(id));

        return result == null
                ? ResponseEntity.status(404).body(ApiResponse.errorResponse("Order with ID '" + id + "' not found."))
                : ResponseEntity.ok(ApiResponse.successResponse(result));
    }
}
