package com.project.liar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.liar.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/mypage", "/updateNickname", "/updateUsername", "/updatePassword", "/logout",
                        "/liar", "/game", "/tetris", "/roulette", "/cll", "/pokerogue")
                .excludePathPatterns("/users/login", "/users/signup", "/index", "/", "/css/**", "/js/**");
    }
}
