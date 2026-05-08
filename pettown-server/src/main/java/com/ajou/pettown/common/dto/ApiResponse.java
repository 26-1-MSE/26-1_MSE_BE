package com.ajou.pettown.common.dto;

// Generic API response wrapper with success flag, data payload, and error message.
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // omit null fields from JSON output
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;

    private ApiResponse(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // Factory for a successful response with a data payload
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // Factory for a failed response with an error message
    public static <T> ApiResponse<T> fail(String error) {
        return new ApiResponse<>(false, null, error);
    }
}
