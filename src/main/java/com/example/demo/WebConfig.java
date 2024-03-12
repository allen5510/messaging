package com.example.demo;

import com.example.demo.controller.intercetor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private GroupInterceptor groupIntercetor;

    @Autowired
    private CheckGroupMembershipInterceptor checkGroupMembershipInterceptor;

    @Autowired
    private CheckParamUserIdExistsInterceptor checkParamUserIdExistsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .excludePathPatterns("/account/login", "/account/register")
                .order(1);

        registry.addInterceptor(groupIntercetor)
                .addPathPatterns("/groups/{group_id}", "/groups/{group_id}/**")
                .order(2);

        registry.addInterceptor(checkGroupMembershipInterceptor)
                .addPathPatterns("/groups/{group_id}/members/**", "/groups/{group_id}/messages", "/groups/{group_id}/messages/**")
                .excludePathPatterns("/groups/{group_id}/members/acceptInvite", "/groups/{group_id}/members/rejectInvite")
                .order(3);

        registry.addInterceptor(checkParamUserIdExistsInterceptor)
                .addPathPatterns("/groups/{group_id}/members", "/groups/{group_id}/members/kickout", "/groups/{group_id}")
                .order(5);
    }
}
