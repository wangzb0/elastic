package com.elastic.entity;

public enum ResponseCodeEnum {
    SUCCESS(0, "请求成功"),
    FAILED(1, "请求失败"),
    NOT_LOGIN(1001, "用户未登录"),
    PERMISSION_DENIED(1002, "权限不足"),
    ;
    private Integer code;
    private String msg;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
