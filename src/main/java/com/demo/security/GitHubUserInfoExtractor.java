package com.demo.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author
 * @date 2025-08-27
 */
public class GitHubUserInfoExtractor implements UserInfoExtractor {

    @Override
    public boolean supports(String registrationId) {
        return "github".equals(registrationId);
    }

    @Override
    public Map<String, Object> extractUserInfo(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        Map<String, Object> userInfo = new HashMap<>();
        // 提取 GitHub 的唯一 ID (id)
        userInfo.put("id", String.valueOf(attributes.get("id")));
        // 提取登录名 (login)
        userInfo.put("username", attributes.get("login"));
        // 提取姓名 (name)
        userInfo.put("name", attributes.get("name"));
        // 提取邮箱 (email)
        userInfo.put("email", attributes.get("email"));
        return userInfo;
    }
}
