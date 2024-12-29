package com.stdnullptr.emailgenerator.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ApiResponse<T> {
    private final T data;
    private final String message;

    private ApiResponse(final String message, final T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<>(null, data);
    }

    public static <T> ApiResponse<T> success(final String message, final T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(final String message) {
        return new ApiResponse<>(message, null);
    }
}
