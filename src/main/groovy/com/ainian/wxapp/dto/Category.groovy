package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    @ManyToOne
    Image topicImg
    String name
    String description
    Long deleteTime
    Long updateTime
}
