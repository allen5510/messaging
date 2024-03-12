package com.example.demo.controller.intercetor;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.exception.OperationException;
import com.example.demo.service.GroupMemberService;
import com.example.demo.controller.util.AttributeKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CheckGroupMembershipInterceptor implements HandlerInterceptor {

    private final GroupMemberService groupMemberService;

    @Autowired
    public CheckGroupMembershipInterceptor(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        validateGroupMembership(request);
        return true;
    }

    private void validateGroupMembership(HttpServletRequest request) throws OperationException {
        long userId = (long) request.getAttribute(AttributeKeys.USER_ID);
        GroupDTO group = (GroupDTO) request.getAttribute(AttributeKeys.GROUP);

        groupMemberService.validateGroupMembership(userId, group.getId());
    }
}
