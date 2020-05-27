package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.Product;
import com.ainian.wxapp.dto.User;
import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.service.OrderService;
import com.ainian.wxapp.service.UserTokenService;
import com.ainian.wxapp.vo.OProduct;
import com.ainian.wxapp.vo.OrderStatus;
import com.ainian.wxapp.vo.UserScope;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    RedisTemplate redisTemplate;


    @PostMapping("/api/v1/order")
    public OrderStatus getToken(@RequestHeader(required = false) String token, List<OProduct> products){


        UserScope userScope = (UserScope)redisTemplate.opsForValue().get(token);
        if(userScope==null) throw new WebException("身份异常");
        User user=userScope.getUser();

        OrderStatus jsonObject = orderService.place(user,products);

        return jsonObject;
    }
}
