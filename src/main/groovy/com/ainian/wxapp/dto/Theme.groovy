package com.ainian.wxapp.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name
    String description
    @ManyToOne
    Image topicImg
    @ManyToOne
    Image headImg
    Long updateTime

    @ManyToMany
    List<Product> products

}
