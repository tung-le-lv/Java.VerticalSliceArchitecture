package com.openmind.order.features.updateorderstatus;

import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.ApiResults;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateOrderStatusEndpoint {

    private final Mediator mediator;

    public UpdateOrderStatusEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<String>> handle(@PathVariable("id") String id, @RequestBody UpdateStatusRequest request) {
        UpdateOrderStatusResult result = mediator.send(new UpdateOrderStatusCommand(id, request.status()));
        return ApiResults.toHttpResult(result);
    }

    public record UpdateStatusRequest(OrderStatus status) {
    }
}
