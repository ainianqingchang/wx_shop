package com.ainian.wxapp.dto

import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
class OrderProductKey implements Serializable{
    @ManyToOne
    Order order
    @ManyToOne
    Product product

    @Override
    int hashCode() {
        return super.hashCode()
    }

    @Override
    boolean equals(Object obj) {
        return super.equals(obj)
    }

    OrderProductKey(Order order, Product product) {
        this.order = order
        this.product = product
    }
    OrderProductKey(){}
}
