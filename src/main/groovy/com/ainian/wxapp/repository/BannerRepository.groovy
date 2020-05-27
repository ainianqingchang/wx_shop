package com.ainian.wxapp.repository

import com.ainian.wxapp.dto.Banner
import org.springframework.data.jpa.repository.JpaRepository

interface BannerRepository extends JpaRepository<Banner,Long>{

}
