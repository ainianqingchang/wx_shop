package com.ainian.wxapp;

import com.ainian.wxapp.interceptor.ExclusiveScope;
import com.ainian.wxapp.interceptor.PrimaryScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
public class WxappApplication {

    @Autowired
    PrimaryScope primaryScope;

    @Autowired
    ExclusiveScope exclusiveScope;

    @Configuration
    public class MvcConfig implements WebMvcConfigurer{
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(primaryScope).addPathPatterns("/api/v1/address");
            registry.addInterceptor(exclusiveScope).addPathPatterns("/api/v1/order","/api/v1/pay/pre_order");
//                    .excludePathPatterns("api/path/login");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(WxappApplication.class, args);
    }
}
