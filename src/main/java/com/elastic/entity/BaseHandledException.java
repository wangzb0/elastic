package com.elastic.entity;

public class BaseHandledException extends RuntimeException {
    public BaseHandledException() {
        super();
    }

    public BaseHandledException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseHandledException(String message) {
        super(message);
    }

    public BaseHandledException(Throwable cause) {
        super(cause);
    }
}
