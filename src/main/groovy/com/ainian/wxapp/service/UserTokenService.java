package com.ainian.wxapp.service;

import com.ainian.wxapp.dto.User;
import com.ainian.wxapp.finvar.ScopeEnum;
import com.ainian.wxapp.repository.UserRepository;
import com.ainian.wxapp.utils.CommonUtils;
import com.ainian.wxapp.utils.MD5Utils;
import com.ainian.wxapp.utils.RestUtils;
import com.ainian.wxapp.vo.UserScope;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserTokenService {

    @Autowired
    private Utils utils;


    @Autowired
    UserRepository userRepository;


    @Autowired
    RedisTemplate redisTemplate;

    @Component
    class Utils{
        private String GET_USER_TOKEN="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

        @Value("${app-id}")
        private String appId;

        @Value("${app-secret}")
        private String appSecret;

        public JSONObject getOpenId(String code){
            String url = GET_USER_TOKEN.replace("APPID", appId)
                    .replace("APPSECRET", appSecret)
                    .replace("JSCODE", code);
            return RestUtils.get(url,JSONObject.class);
        }
    }

    @Value("token-salt")
    private String tokenSalt;


    public JSONObject generateToken(String code){
        JSONObject result = utils.getOpenId(code);
        String openId=result.getString("openid");
        User byOpenId = userRepository.findByOpenId(openId);
        if(byOpenId==null){
            byOpenId=new User();
            byOpenId.setOpenId(openId);
            byOpenId.setCreateTime(System.currentTimeMillis());
            userRepository.saveAndFlush(byOpenId);
        }
        String randomString = CommonUtils.getRandomString(32);
        Long timesamp=System.currentTimeMillis();
        String token = MD5Utils.encode(randomString + String.valueOf(timesamp) + tokenSalt);
        JSONObject json=new JSONObject();
        UserScope userScope=new UserScope();
        userScope.setUser(byOpenId);
        userScope.setScope(ScopeEnum.USER);
        redisTemplate.opsForValue().set(token,userScope,7200, TimeUnit.SECONDS);
        json.put("token",token);
        json.put("expire",7200);

        return json;
    }


}
