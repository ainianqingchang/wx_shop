package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class ThirdApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String appId
    String appSecret
    String appDescription
    String scope
    String scopeDescription
    Long deleteTime
    Long updateTime

}
