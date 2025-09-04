package com.astrapay.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BaseResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}