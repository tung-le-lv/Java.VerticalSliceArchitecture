package com.openmind.order.features.getordersbydaterange;

import com.openmind.order.shared.ApiResponse;
import com.openmind.order.shared.application.dto.OrderDto;
import com.openmind.order.shared.mediator.Mediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GetOrdersByDateRangeEndpoint {

    private final Mediator mediator;

    public GetOrdersByDateRangeEndpoint(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/orders/filter")
    public ResponseEntity<ApiResponse<List<OrderDto>>> handle(@RequestParam(required = false) String date) {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.errorResponse("Query parameter 'date' must be a valid date (YYYY-MM-DD)."));
        }

        List<OrderDto> result = mediator.send(new GetOrdersByDateRangeQuery(parsedDate));
        return ResponseEntity.ok(ApiResponse.successResponse(result));
    }
}
