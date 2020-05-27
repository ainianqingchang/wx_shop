package com.ainian.wxapp.repository


import com.ainian.wxapp.dto.OrderProduct
import com.ainian.wxapp.dto.OrderProductKey
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey>{
    @Query('''select o from OrderProduct o 
              where o.id.order=:orderId''')
    List<OrderProduct> findByOrderId(@Param("orderId") Long orderId)
}
