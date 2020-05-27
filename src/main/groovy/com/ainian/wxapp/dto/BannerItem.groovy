package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class BannerItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    @ManyToOne
    Image img
    @ManyToOne
    Banner banner
    String keyWord
    Integer Type
    Long deleteTime
    Long updateTime
}
