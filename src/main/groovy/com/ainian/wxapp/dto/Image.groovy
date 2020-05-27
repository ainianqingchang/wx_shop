package com.ainian.wxapp.dto

import com.ainian.wxapp.utils.SpringUtils
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String url
    Integer from
    Long deleteTime
    Long updateTime


    String getUrl(){
        return SpringUtils.getServerAddress()+url
    }

}
