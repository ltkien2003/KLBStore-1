package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.klbstore.jwt.JwtAuthenticationFilter;
import com.klbstore.model.UserService;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/user/index").permitAll()
                .antMatchers("/rest/xinchao").permitAll()
                .antMatchers("/rest/hello").permitAll()
                .antMatchers("/add").permitAll()
                .antMatchers("/update").permitAll()
                .antMatchers("/delete").permitAll()
                .antMatchers("/giohanginfo").permitAll()
                .antMatchers("/findColorId").permitAll()
                .antMatchers("/findColorId").permitAll()
                .antMatchers("/rest/sanpham").permitAll()
                .antMatchers("/rest/canUserRateProduct").permitAll()
                .antMatchers("/rest/danhGia").permitAll()
                .antMatchers("/user/search").permitAll()
                .antMatchers("/user/contact").permitAll()
                .antMatchers("/user/shop-list").permitAll()
                .antMatchers("/user/product-details").permitAll()
                .antMatchers("/user/shopping-cart").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/forgot-password/**").permitAll()
                .antMatchers("/user/active/**").permitAll()
                .antMatchers("/user/change-password").permitAll()
                .antMatchers("/user/checkout").permitAll()
                .anyRequest().authenticated();
    
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
