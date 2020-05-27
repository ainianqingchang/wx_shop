package com.ainian.wxapp.finvar;

public enum ErrorCode {
    //调用错误码
    WEB_LOGIN_ERR(10001, "用户名或密码错误"),
    WEB_NOT_LOGIN(10002, "未登录"),
    WEB_ACCESS_DENY(10003, "禁止访问"),
    ORDER_NOT_FOUND(10101, "订单不存在，请检查！"),
    UNKNOWN_ERROR(11000,"未知错误");


    private String msg;
    private int code;

    ErrorCode(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}