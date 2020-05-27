package com.ainian.wxapp.exception;

import com.ainian.wxapp.finvar.ErrorCode;

public class WebException extends RuntimeException {
    private int code;
    private String msg;

    public WebException() {
    }

    public WebException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public WebException(ErrorCode code){
        this(code.getCode(),code.getMsg());
    }



    public WebException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
    }

    public WebException(Throwable throwable) {
        super(throwable);
    }

    protected WebException(String msg, Throwable throwable, boolean b, boolean b1) {
        super(msg, throwable, b, b1);
        this.msg = msg;
    }

    public WebException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}