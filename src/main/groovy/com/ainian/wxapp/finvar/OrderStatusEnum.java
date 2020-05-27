package com.ainian.wxapp.finvar;

public enum  OrderStatusEnum {
    UNPAID(1),PAID(2),DELIVERED(3),PAID_BUT_OUT_OF(4);

    private Integer code;

    OrderStatusEnum(Integer code){
        this.code=code;
    }

    public Integer getCode(){
        return this.code;
    }
}
