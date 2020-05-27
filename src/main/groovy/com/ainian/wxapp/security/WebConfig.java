package com.ainian.wxapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ainianqingchang
 * @classname WebConfig
 * @description TODO
 * @date 2018/11/24 19:17
 */

@Configuration
public class WebConfig {


    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private SuccessHandler successHandler;

        @Autowired
        FailureHandler failureHandler;

        @Autowired
        WebLogoutSuccessHandler webLogoutSuccessHandler;

        @Autowired
        private PasswordEncoder noPasswordEncoder;

        @Autowired
        private UserDetailsService webUserDetailsService;


        @Override
        @Order(Ordered.HIGHEST_PRECEDENCE + 1) //必须要有这个注解 表示这个配置优先加载
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().antMatchers("/login", "/oauth/**", "/api/**", "/image/**").permitAll()
                    .and().authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .and()
                    .formLogin()
//                    .loginPage("/login")
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(webLogoutSuccessHandler)
                    .deleteCookies("JSESSIONID")
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(webUserDetailsService).passwordEncoder(noPasswordEncoder);

        }


    }

    @Service(value = "webUserDetailsService")
    public class WebUserDetailsService implements UserDetailsService {

        @Autowired
        private PasswordEncoder noPasswordEncoder;


        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return new User("admin", noPasswordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));

        }

        @Bean
        public PasswordEncoder NoPasswordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }


    }

    @Service
    public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {



        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
            response.setContentType("text/json; charset=utf-8");
            HttpSession session=request.getSession();
            String username=authentication.getName();
            logger.info("WEB:"+username+" 用户登录成功！");

//            response.sendRedirect("index");
        }
    }


    @Service
    public class FailureHandler implements AuthenticationFailureHandler {



        @Override
        public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
            httpServletResponse.setContentType("text/json; charset=utf-8");
            HttpSession session=httpServletRequest.getSession();

        }
    }

    @Service
    public class WebLogoutSuccessHandler implements LogoutSuccessHandler{

        @Override
        public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            httpServletResponse.setContentType("text/json; charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();

        }
    }
}
