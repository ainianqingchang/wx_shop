package com.ainian.wxapp.repository


import com.ainian.wxapp.dto.User
import com.ainian.wxapp.dto.UserAddress
import org.springframework.data.jpa.repository.JpaRepository

interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
    UserAddress findByUser(User user);
}