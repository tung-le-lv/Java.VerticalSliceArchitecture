package com.openmind.order.features.getordersbycustomerandstatus;

import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GetOrdersByCustomerAndStatusEndpoint {

    private final Mediator mediator;

    public GetOrdersByCustomerAndStatusEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/orders/customer/{customerId}/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> handle(@PathVariable String customerId, @PathVariable String status) {
        OrderStatus parsedStatus;
        try {
            parsedStatus = OrderStatus.parseIgnoreCase(status);
        } catch (IllegalArgumentException ex) {
            String validValues = Arrays.stream(OrderStatus.values()).map(Enum::name).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(ApiResponse.errorResponse(
                    "Invalid status '" + status + "'. Valid values: " + validValues));
        }

        List<OrderDto> result = mediator.send(new GetOrdersByCustomerAndStatusQuery(customerId, parsedStatus));
        return ResponseEntity.ok(ApiResponse.successResponse(result));
    }
}
