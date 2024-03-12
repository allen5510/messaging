package com.example.demo.controller.intercetor;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.exception.FormatException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.GroupService;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.controller.util.RequestKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GroupInterceptor implements HandlerInterceptor {

    private final GroupService groupService;

    @Autowired
    public GroupInterceptor(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        assignGroupToRequest(request);
        return true;
    }

    private void assignGroupToRequest(HttpServletRequest request) throws FormatException, NotFoundException {
        String groupIdStr = InterceptorHelper.extractVariableByUrl(request, RequestKeys.GROUP_ID);
        long groupId = InterceptorHelper.parseIdAndThrow(groupIdStr);
        GroupDTO group = groupService.findGroupDTO(groupId);
        request.setAttribute(AttributeKeys.GROUP, group);
    }

}
