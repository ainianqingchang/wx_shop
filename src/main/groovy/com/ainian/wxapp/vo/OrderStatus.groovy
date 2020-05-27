package com.ainian.wxapp.vo

class OrderStatus {
    Boolean pass=true
    BigDecimal orderPrice=new BigDecimal(0)
    List<ProductStatus> pStatus=new LinkedList<>()
    Long orderId=-1L
    String orderNo=""
    Long createTime=0
    Long totalCount=0
}
