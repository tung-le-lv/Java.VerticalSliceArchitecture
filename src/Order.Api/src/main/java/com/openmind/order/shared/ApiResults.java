package com.openmind.order.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResults
{
    private ApiResults()
    {
    }

    public static ResponseEntity<ApiResponse<String>> toHttpResult(OperationResult result)
    {
        return toHttpResult(result, "OK");
    }

    public static ResponseEntity<ApiResponse<String>> toHttpResult(OperationResult result, String successMessage)
    {
        if (result.success())
        {
            return ResponseEntity.ok(ApiResponse.successResponse(successMessage, result.message()));
        }

        HttpStatus statusCode = result.message() != null && result.message().toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(statusCode).body(ApiResponse
                .errorResponse(result.message() != null ? result.message() : "Request failed.", result.errors()));
    }
}
