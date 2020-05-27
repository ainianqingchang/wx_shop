package com.ainian.wxapp.repository


import com.ainian.wxapp.dto.Theme
import com.ainian.wxapp.dto.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository extends JpaRepository<User,Long> {
    User findByOpenId(String openId);
}