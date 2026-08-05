package com.openmind.order.features.cancelorder;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.ApiResults;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CancelOrderEndpoint {

    private final Mediator mediator;

    public CancelOrderEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> handle(@PathVariable("id") String id) {
        CancelOrderResult result = mediator.send(new CancelOrderCommand(id));
        return ApiResults.toHttpResult(result);
    }
}
