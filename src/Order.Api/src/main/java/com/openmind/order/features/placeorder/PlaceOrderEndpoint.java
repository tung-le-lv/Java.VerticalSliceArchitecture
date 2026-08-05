package com.openmind.order.features.placeorder;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.ApiResults;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceOrderEndpoint
{

    private final Mediator mediator;

    public PlaceOrderEndpoint(Mediator mediator)
    {
        this.mediator = mediator;
    }

    @PostMapping("/orders/{id}/place")
    public ResponseEntity<ApiResponse<String>> handle(@PathVariable("id") String id)
    {
        PlaceOrderResult result = mediator.send(new PlaceOrderCommand(id));
        return ApiResults.toHttpResult(result);
    }
}
