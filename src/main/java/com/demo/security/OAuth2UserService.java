package com.demo.security;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2025-08-27
 */
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(OAuth2UserService.class);

    @Autowired
    private UserService userService;

    // 注入所有实现了 UserInfoExtractor 接口的 Bean
    @Autowired
    private List<UserInfoExtractor> userInfoExtractors;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // 找到正确的用户信息提取器
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Optional<UserInfoExtractor> extractor = userInfoExtractors.stream()
                .filter(e -> e.supports(registrationId))
                .findFirst();

        if (extractor.isPresent()) {
            Map<String, Object> userInfo = extractor.get().extractUserInfo(oauth2User);

            String socialId = (String) userInfo.get("id"); // 用户ID
            String email = (String) userInfo.get("email"); // 注册邮箱
            String name = (String) userInfo.get("name"); // 用户名

            logger.info("oauth2 login : socialId {} email {} name {}", socialId, email, name);

            // 使用通用的 ID 和邮件地址进行用户登录/注册逻辑
            // 查找用户是否存在
            User user = userService.findBySocialIdAndProvider(socialId, registrationId);

            if (user == null) {
                userService.createUser(email, "password");
            }
        }
        return oauth2User;
    }
}
