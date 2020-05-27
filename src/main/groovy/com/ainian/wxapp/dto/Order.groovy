package com.ainian.wxapp.dto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne
import java.text.DecimalFormat

@Entity
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String orderNo
    @ManyToOne
    User user
    Long deleteTime
    Long createTime
    @Column(precision = 6,scale = 2)
    BigDecimal totalPrice
    Integer status
    String snapImg
    String snapName
    Long totalCount
    Long updateTime
    @Lob
    @Column(columnDefinition="text")
    String snapItems
    @Column(columnDefinition="varchar(500)")
    String snapAddress
    String prepayId
}
