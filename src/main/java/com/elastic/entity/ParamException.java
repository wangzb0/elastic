package com.elastic.entity;

/**
 * @description 参数错误
 **/
public class ParamException extends BaseHandledException {
    public ParamException() {
        super();
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }
}
