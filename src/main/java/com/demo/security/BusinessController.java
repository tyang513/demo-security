package com.demo.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author
 * @date 2025-08-27
 */
@Controller
@RequestMapping("/buss")
public class BusinessController {

    @RequestMapping(value = "/example" , method = GET)
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("userName");
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("returnMap", "example");
        returnMap.put("username", username);
        returnMap.put("sessionId", request.getSession().getId());
        return returnMap;
    }

}
