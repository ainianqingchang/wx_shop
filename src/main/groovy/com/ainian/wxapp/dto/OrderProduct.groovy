package com.ainian.wxapp.dto

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class OrderProduct {
    @EmbeddedId
    OrderProductKey id
    Long count
    Long deleteTime
    Long updateTime

}
