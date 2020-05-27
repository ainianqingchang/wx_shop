package com.ainian.wxapp.repository


import com.ainian.wxapp.dto.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository extends JpaRepository<Order,Long>{

    Order findByOrderNo(String orderNo)
}
