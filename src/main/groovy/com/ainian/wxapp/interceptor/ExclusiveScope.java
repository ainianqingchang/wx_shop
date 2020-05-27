package com.ainian.wxapp.interceptor;

import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.finvar.ErrorCode;
import com.ainian.wxapp.finvar.ScopeEnum;
import com.ainian.wxapp.vo.UserScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ExclusiveScope implements HandlerInterceptor {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("token");
        UserScope userScope=(UserScope) redisTemplate.opsForValue().get(token);
        if(userScope==null) throw new WebException(ErrorCode.WEB_NOT_LOGIN);
        ScopeEnum scope=userScope.getScope();
        if(scope != ScopeEnum.USER){

            throw new WebException(ErrorCode.WEB_ACCESS_DENY);
        }
        else{
            request.setAttribute("user",userScope.getUser());
            request.setAttribute("scope",userScope.getScope());
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
