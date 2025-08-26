package com.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author
 * @date 2025-08-26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*--------------------------------------- 使用 spring security 的登录页面 ---------------------------------------*/
//        http.csrf().disable().authorizeRequests()
//                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin();

        /*--------------------------------------- 自定义页面 ---------------------------------------*/
        // 自定义登录页面时，需要注意 /login接口需要在页面、antMatchers 中放行，同时loginProcessingUrl 中指定，这三个地方必须一致才可，否则自定义页面是不成功的
        http.csrf().disable().authorizeRequests()
                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
                .antMatchers("/", "/login-page.html", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login-page.html").loginProcessingUrl("/login")
                .defaultSuccessUrl("/home.html", false);

    }

    /**
     * 放行静态资源
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        // 静态资源直接放行
        web.ignoring().antMatchers(
                "/favicon.ico",
                "/css/**",
                "/js/**",
                "/images/**"
        );
    }


}
