package com.elastic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;


@Data
public class RestResponse<T> implements Serializable {
    private static final long serialVersionUID = -5036207454231061658L;
    private int code;
    private String message;
    private T result;

    public RestResponse() {
        this(0, "操作成功", null);
    }

    public RestResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = null;
    }

    public RestResponse(int code, String message, T result) {
        this.setCode(code);
        this.setMessage(message);
        this.setResult(result);
    }


    public void success(T result) {
        this.code = 0;
        this.message = "操作成功";
        this.result = result;
    }

    public void success(T result, String msg) {
        this.code = 0;
        this.message = msg;
        this.result = result;
    }

    public void failed(String message) {
        this.failed(1, message);
    }

    public void failed(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = null;
    }

    @JsonIgnore
    public boolean isSuccess() {
        if (this.getCode() == ResponseCodeEnum.SUCCESS.getCode()) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    @NonNull
    public T getResultElseThrow() {
        return getResultElseThrow(this.message);
    }

    @JsonIgnore
    @NonNull
    public T getResultElseThrow(String message) {
        if (this.code == ResponseCodeEnum.SUCCESS.getCode() && this.result != null) {
            return this.result;
        } else {
            throw new BadResponseException(this.code, message);
        }
    }

    public static <T> RestResponse<T> of(@NonNull T result) {
        RestResponse<T> response = new RestResponse<>();
        response.success(result);
        return response;
    }

    public static <T> RestResponse<T> ofError(String message) {
        RestResponse<T> response = new RestResponse<>();
        response.failed(message);
        return response;
    }

    public static <T> RestResponse<T> ofError(int code, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.failed(code, message);
        return response;
    }
}
