package com.ainian.wxapp.dto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name
    @Column(precision = 6,scale = 2)
    BigDecimal price
    Long stock
    @ManyToOne
    Category category
    String mainImgUrl
    Integer from
    String summary
    @ManyToOne
    Image img
    Long createTime
    Long updateTime
    Long deleteTime
    @ManyToMany(mappedBy = "products")
    List<Theme> themes
    @OneToMany(mappedBy = "product")
    ProductImage imgs

    @OneToMany(mappedBy = "product")
    ProductProperty properties
}
