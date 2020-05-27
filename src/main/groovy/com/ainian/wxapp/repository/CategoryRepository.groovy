package com.ainian.wxapp.repository

import com.ainian.wxapp.dto.Category
import com.ainian.wxapp.dto.Product
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository extends JpaRepository<Category,Long> {

}