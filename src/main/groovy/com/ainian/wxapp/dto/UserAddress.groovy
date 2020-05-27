package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name
    String mobile
    String province
    String city
    String country
    String detail
    Long deleteTime
    @ManyToOne
    User user
    Long updateTime
}
