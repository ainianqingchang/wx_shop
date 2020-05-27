package com.ainian.wxapp.repository

import com.ainian.wxapp.dto.Category
import com.ainian.wxapp.dto.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategory(Category category)
}