package com.research.randy.apiResponse.util;

import com.research.randy.apiResponse.apiResponse;

public class apiResponseUtil {
    public static <T> apiResponse<T> createSuccessResponse(T result) {
        return new apiResponse<>("00", "Success", result);
    }
    public static <T> apiResponse<T> createErrorResponse(String message) {
        return new apiResponse<>("01", message, null);
    }
}
