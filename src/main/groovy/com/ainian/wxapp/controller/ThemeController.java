package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.Banner;
import com.ainian.wxapp.dto.Theme;
import com.ainian.wxapp.repository.BannerRepository;
import com.ainian.wxapp.repository.ThemeRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ThemeController {
    @Autowired
    ThemeRepository themeRepository;

    @GetMapping("/api/v1/theme")
    public JSONArray getThemeList(String id){
        String[] ids=id.split(",");
        List<Theme> themeList=new ArrayList<>();
        for(String e:ids){
            Theme one = themeRepository.getOne(Long.valueOf(e));
            if(one!=null) themeList.add(one);
        }

        JSONArray array=new JSONArray();
        for(Theme e:themeList){
            JSONObject json=new JSONObject();
            json.put("id",e.getId());
            json.put("name",e.getName());
            json.put("description",e.getDescription());
            json.put("topic_img_id",e.getTopicImg().getId());
            json.put("topic_img",e.getTopicImg());
            json.put("head_img",e.getHeadImg());
            array.add(json);
        }
        return array;
    }


    @GetMapping("/api/v1/theme/{id}")
    public Theme getThemeDetail(@PathVariable Long id){
        Theme one = themeRepository.getOne(id);
        return one;
    }

}
