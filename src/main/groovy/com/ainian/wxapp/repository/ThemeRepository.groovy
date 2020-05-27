package com.ainian.wxapp.repository

import com.ainian.wxapp.dto.Banner
import com.ainian.wxapp.dto.Theme
import org.springframework.data.jpa.repository.JpaRepository

interface ThemeRepository extends JpaRepository<Theme,Long> {

}