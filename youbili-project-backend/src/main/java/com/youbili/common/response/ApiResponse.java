package com.youbili.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(int status, boolean success, String message, T data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ServiceCode.OK.getCode(), true, ServiceCode.OK.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ServiceCode.OK.getCode(), true, ServiceCode.OK.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ServiceCode.OK.getCode(), true, message, data);
    }

    public static <T> ApiResponse<T> failure(ServiceCode serviceCode) {
        return new ApiResponse<>(serviceCode.getCode(), false, serviceCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> failure(ServiceCode serviceCode, String message) {
        return new ApiResponse<>(serviceCode.getCode(), false, message, null);
    }
}
