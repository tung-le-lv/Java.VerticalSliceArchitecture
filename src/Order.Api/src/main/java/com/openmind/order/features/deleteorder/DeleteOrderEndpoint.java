package com.openmind.order.features.deleteorder;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteOrderEndpoint
{

    private final Mediator mediator;

    public DeleteOrderEndpoint(Mediator mediator)
    {
        this.mediator = mediator;
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<String>> handle(@PathVariable("id") String id)
    {
        DeleteOrderResult result = mediator.send(new DeleteOrderCommand(id));

        return result.success()
                ? ResponseEntity.ok(ApiResponse.successResponse("OK", result.message()))
                : ResponseEntity.badRequest().body(ApiResponse
                        .errorResponse(result.message() != null ? result.message() : "Failed to delete order."));
    }
}
