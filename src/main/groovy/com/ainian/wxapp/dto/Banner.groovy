package com.ainian.wxapp.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name
    String description
    Long deleteTime
    Long updateTime
    @OneToMany(mappedBy = "banner")
    @JsonIgnoreProperties(value ="banner")
    List<BannerItem> items
}
