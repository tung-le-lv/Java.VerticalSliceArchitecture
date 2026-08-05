package com.openmind.order.shared;

import java.util.List;

public record ApiResponse<T>(boolean success, T data, String message, List<String> errors) {

    public static <T> ApiResponse<T> successResponse(T data) {
        return successResponse(data, null);
    }

    public static <T> ApiResponse<T> successResponse(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> errorResponse(String message) {
        return errorResponse(message, null);
    }

    public static <T> ApiResponse<T> errorResponse(String message, List<String> errors) {
        return new ApiResponse<>(false, null, message, errors);
    }
}
