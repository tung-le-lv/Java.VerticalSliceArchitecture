package com.openmind.order.features.addorderitem;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.ApiResults;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class AddOrderItemEndpoint {

    private final Mediator mediator;

    public AddOrderItemEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/orders/{id}/items")
    public ResponseEntity<ApiResponse<String>> handle(@PathVariable("id") String id, @RequestBody AddItemRequest request) {
        AddOrderItemCommand command = new AddOrderItemCommand(id, request.productId(), request.productName(), request.quantity(), request.unitPrice());
        AddOrderItemResult result = mediator.send(command);
        return ApiResults.toHttpResult(result);
    }

    public record AddItemRequest(String productId, String productName, int quantity, BigDecimal unitPrice) {
    }
}
