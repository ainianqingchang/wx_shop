package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.Category;
import com.ainian.wxapp.dto.Product;
import com.ainian.wxapp.repository.ProductRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/api/v1/product/recent")
    public JSONArray getRecent(String count){
        Integer countL=Integer.valueOf(count);
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable= PageRequest.of(1,countL,sort);
        Page<Product> all = productRepository.findAll(pageable);
        List<Product> content = all.getContent();

        JSONArray array=new JSONArray();
        for(Product e:content){
            JSONObject json=new JSONObject();
            json.put("id",e.getId());
            json.put("name",e.getName());
            json.put("price",e.getPrice());
            json.put("stock",e.getStock());
            json.put("summary",e.getSummary());
            json.put("img_id",e.getImg().getId());
            array.add(json);
        }
        return array;
    }

    @GetMapping("/api/v1/product/by_category")
    public JSONArray getByCategory(Long id){
        Category category=new Category();
        category.setId(id);
        List<Product> products = productRepository.findByCategory(category);

        JSONArray array=new JSONArray();
        for(Product e:products){
            JSONObject json=new JSONObject();
            json.put("id",e.getId());
            json.put("name",e.getName());
            json.put("price",e.getPrice());
            json.put("stock",e.getStock());
            json.put("main_img_url",e.getImg().getUrl());
            json.put("img_id",e.getImg().getId());
            array.add(json);
        }
        return array;
    }

    @GetMapping("/api/v1/product/{id}")
    public Product getProduct(@PathVariable Long id){
        Product one = productRepository.getOne(id);
        return one;
    }

}
