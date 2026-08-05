package com.openmind.order.features.getordersbycustomer;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetOrdersByCustomerEndpoint
{

    private final Mediator mediator;

    public GetOrdersByCustomerEndpoint(Mediator mediator)
    {
        this.mediator = mediator;
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> handle(@PathVariable String customerId)
    {
        List<OrderDto> result = mediator.send(new GetOrdersByCustomerQuery(customerId));
        return ResponseEntity.ok(ApiResponse.successResponse(result));
    }
}
