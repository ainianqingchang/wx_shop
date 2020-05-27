package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.User;
import com.ainian.wxapp.dto.UserAddress;
import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.repository.UserAddressRepository;
import com.ainian.wxapp.vo.UserScope;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAddressController {

    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    RedisTemplate redisTemplate;


    @PostMapping("/api/v1/address")
    public JSONObject createOrUpdateAddress(@RequestHeader(required = false) String token,  JSONObject object){
        String name=object.getString("name");
        String mobile=object.getString("mobile");
        String province=object.getString("province");
        String city=object.getString("city");
        String country=object.getString("country");
        String detail=object.getString("detail");

        UserScope userScope = (UserScope)redisTemplate.opsForValue().get(token);
        if(userScope==null) throw new WebException("身份异常");
        User user=userScope.getUser();

        UserAddress userAddress = userAddressRepository.findByUser(user);
        if(userAddress==null) userAddress=new UserAddress();
        userAddress.setCity(city);
        userAddress.setCountry(country);
        userAddress.setMobile(mobile);
        userAddress.setProvince(province);
        userAddress.setName(name);
        userAddress.setDetail(detail);

        JSONObject json =new JSONObject();
        json.put("errcode",0);
        json.put("errmsg","ok");

        return json;
    }
}
