package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String openId
    String nickName
    String extend
    Long deleteTime
    Long createTime
    Long updateTime
}
