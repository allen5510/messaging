package com.example.demo.controller.intercetor;

import com.example.demo.controller.util.RequestKeys;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CheckParamUserIdExistsInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Autowired
    public CheckParamUserIdExistsInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<String> userIdStrOpt = extractUserIdFromRequestParameter(request);
        if(userIdStrOpt.isPresent()){
            long userId = InterceptorHelper.parseIdAndThrow(userIdStrOpt.get());
            userService.validateUserExistence(userId);
        }
        return true;
    }

    private Optional<String> extractUserIdFromRequestParameter(HttpServletRequest request) {
        return Stream.of(RequestKeys.NEW_MEMBER, RequestKeys.NEW_OWNER, RequestKeys.KICKOUT)
                .map(request::getParameter)
                .filter(Objects::nonNull)
                .findFirst();
    }

}
