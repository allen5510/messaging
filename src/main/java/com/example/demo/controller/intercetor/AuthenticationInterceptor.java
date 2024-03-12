package com.example.demo.controller.intercetor;

import com.example.demo.exception.TokenException;
import com.example.demo.service.UserService;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.util.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public AuthenticationInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractAuthToken(request);
        long userId = TokenProcessor.parseUserIdAndThrow(token);
        userService.validateUserExistence(userId);
        request.setAttribute(AttributeKeys.USER_ID, userId);
        return true;
    }

    private String extractAuthToken(HttpServletRequest request) throws TokenException {
        String token = request.getHeader("Auth-Token");
        if (token == null || token.isEmpty()) {
            throw new TokenException("Auth-Token not found");
        }
        return token;
    }

}
