package com.ainian.wxapp.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    @ManyToOne
    Image img
    Long deleteTime
    Long order

    @ManyToOne
    @JsonIgnoreProperties(value =["imgs","properties"])
    Product product
}
