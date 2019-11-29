package com.elastic.entity;

public class BadResponseException extends BaseHandledException {

    public BadResponseException(int code, String message) {
        super(String.format("错误代码 %d: %s", code, message));
    }

}
