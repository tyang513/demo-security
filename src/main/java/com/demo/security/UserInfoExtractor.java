package com.demo.security;

import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Map;

public interface UserInfoExtractor {
    // 判断是否支持某个注册 ID（如 "github", "google"）
    boolean supports(String registrationId);
    
    // 从 OAuth2User 中提取并返回通用的用户信息
    Map<String, Object> extractUserInfo(OAuth2User oauth2User);
}