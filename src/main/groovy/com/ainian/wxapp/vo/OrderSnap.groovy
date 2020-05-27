package com.ainian.wxapp.vo

import com.ainian.wxapp.dto.UserAddress

class OrderSnap {
    BigDecimal orderPrice=new BigDecimal(0)
    Long totalCount=0
    List<ProductStatus> pStatus
    UserAddress userAddress
    String snapName
    String snapImg
}
