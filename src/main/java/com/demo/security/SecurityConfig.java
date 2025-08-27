package com.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

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
        //  登录成功后跳转,如果 defaultSuccessUrl alwaysUse=true,则登录成功后强制跳转到/home页; alwaysUse=false 如果访问了受保护页面,则跳转到受保护页,否则跳转到/home页
//        http.csrf().disable().authorizeRequests()
//                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().defaultSuccessUrl("/home.html", false);

        /*--------------------------------------- 自定义页面 ---------------------------------------*/
        // 自定义登录页面时，需要注意 /login接口需要在页面、antMatchers 中放行，同时loginProcessingUrl 中指定，这三个地方必须一致才可，否则自定义页面是不成功的
        // 这里使用的 /login 是 spring security 自带的登录地址
//        http.csrf().disable().authorizeRequests()
//                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
//                .antMatchers("/", "/login-page.html", "/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login-page.html")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/home.html", false);

        /*--------------------------------------- 自定义页面 && 自定义登录端点 ---------------------------------------*/
        // 这种方式并会使用自己定义的controller，只是将 spring security 定义的登录端点由 /login 改为 /authorization/login-form, 其实跟上面的实现方法都是一样的
//        http.csrf().disable().authorizeRequests()
//                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
//                .antMatchers("/", "/auth-login.html", "/authorization/login-form").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/auth-login.html")
//                .loginProcessingUrl("/authorization/login-form")
//                .defaultSuccessUrl("/home.html", false).and().logout().permitAll();


        /*--------------------------------------- 自定义页面 && 自定义登录端点 ---------------------------------------*/
        http.csrf().disable().authorizeRequests()
                // 开放登录页面 & OAuth2 登录入口 & 静态资源入口
                .antMatchers("/", "/auth-login.html", "/authorization/login-form").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/auth-login.html")
                .and().
                    logout().permitAll();

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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpSessionRequestCache requestCache() {
        return new HttpSessionRequestCache();
    }
}
