package com.rakhimov.homework.dto;

public record ResponseDto<T>(
        String message,
        T data,
        String errorDetails) {

    public static <T> ResponseDto<T> success(T data, String message) {
        return new ResponseDto<>(message, data, null);
    }

    public static <T> ResponseDto<T> error(String message, String errorDetails) {
        return new ResponseDto<>(message, null, errorDetails);
    }
}
