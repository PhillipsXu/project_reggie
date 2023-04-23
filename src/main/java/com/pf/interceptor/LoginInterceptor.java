package com.pf.interceptor;

import com.alibaba.fastjson.JSON;
import com.pf.utils.BaseContext;
import com.pf.utils.R;
import com.pf.utils.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestToken = null;
        String username = null;
        Cookie[] cookies;
        if (request.getCookies() != null) {
            cookies = request.getCookies();
        } else {
            response.setStatus(400);
            response.getWriter().write(JSON.toJSONString(R.fail("Cookie未设置！")));
            return false;
        }
        log.info("========================================== Start ==========================================");
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                requestToken = cookie.getValue();
                log.info("requestToken   : {}", requestToken);
            }
            if ("username".equals(cookie.getName())) {
                username = cookie.getValue();
                log.info("requestUsername: {}", username);
                // 线程中设置请求者的username
                BaseContext.setRequestUser(username);
            }
        }
        if (requestToken != null && username != null && tokenUtil.verifyToken(username, requestToken)) {
            return true;
        }
        String user = (String) request.getSession().getAttribute("user");
        if (user != null) {
            BaseContext.setRequestUser(user);
            return true;
        }
        response.setStatus(403);
        response.getWriter().write(JSON.toJSONString(R.fail("NOT LOGIN")));
        return false;
    }
}
