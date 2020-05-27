package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.Banner;
import com.ainian.wxapp.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BannerController {
    @Autowired
    BannerRepository bannerRepository;

    @GetMapping("/api/v1/banner/{id}")
    public Banner getBanner(@PathVariable Long id){
        Banner banner = bannerRepository.findById(id).orElse(null);
        return banner;
    }

}
