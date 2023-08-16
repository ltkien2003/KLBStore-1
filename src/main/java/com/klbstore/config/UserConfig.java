package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.klbstore.dao.NguoiDungDAO;
@Configuration
public class UserConfig implements WebMvcConfigurer{

    @Autowired
    NguoiDungDAO nguoiDungDAO;

    @Bean
    public User user() {
        return new User(nguoiDungDAO);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(user());
    }



}
