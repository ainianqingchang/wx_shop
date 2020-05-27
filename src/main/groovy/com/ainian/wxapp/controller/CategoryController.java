package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.Category;
import com.ainian.wxapp.dto.Product;
import com.ainian.wxapp.repository.CategoryRepository;
import com.ainian.wxapp.repository.ProductRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/api/v1/category/all")
    public List<Category> getRecent(){

        List<Category> all = categoryRepository.findAll();


        return all;
    }
}
