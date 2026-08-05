package com.openmind.order.features.getallorders;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAllOrdersEndpoint {

    private final Mediator mediator;

    public GetAllOrdersEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> handle() {
        List<OrderDto> result = mediator.send(new GetAllOrdersQuery());
        return ResponseEntity.ok(ApiResponse.successResponse(result));
    }
}
