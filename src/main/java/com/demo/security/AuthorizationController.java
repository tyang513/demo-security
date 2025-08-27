package com.demo.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 * @date 2025-08-27
 */
@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSessionRequestCache requestCache;

    // method = POST
    @RequestMapping(value = "/login-form")
    public String loginForm(HttpServletRequest request, HttpServletResponse response, @RequestParam String username,
                                     @RequestParam String password) throws IOException {
        logger.info("自定义登录端点 /auth/login username {} password {}", username, password);

        if (username == null || "".equals(username) || password == null || "".equals(password)){
            return "redirect:/auth-login.html?error=请输入用户名或密码";
        }

        // 1. 检查用户是否存在
        UserDetails userDetails = userService.findByUsername(username);
        if (userDetails == null) {
            userService.createUser(username, password);
        }

        try {
            // 3. 验证密码
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authRequest);

            // 4. 将认证信息写入安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. 将认证信息绑定到当前会话，完成登录
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            // 核心逻辑：获取并重定向到已保存的请求
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                // 如果有已保存的请求，重定向到该请求的 URL
                return "redirect:" + savedRequest.getRedirectUrl();
            } else {
                // 如果没有已保存的请求（例如直接访问登录页），重定向到默认主页
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/auth-login.html?error=" + e.getMessage();
        }
    }

    @RequestMapping(value = "/oauth2/github")
    public String oauth2Github(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return null;
    }
}
