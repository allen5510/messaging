package com.example.demo.controller;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.controller.DTO.GroupMemberDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.OperationException;
import com.example.demo.controller.util.RequestKeys;
import com.example.demo.service.GroupMemberService;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.controller.util.ResponseHandler;
import com.example.demo.controller.util.ResponseKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/groups/{"+RequestKeys.GROUP_ID+"}/members")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    @Autowired
    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @GetMapping
    public ResponseEntity queryUsers(@PathVariable(name = RequestKeys.GROUP_ID) long groupId)  {
        Set<GroupMemberDTO> users = groupMemberService.queryMembersByGroupId(groupId);
        return ResponseHandler.responseQueryResults(ResponseKeys.USERS, users);
    }

    @PostMapping
    public ResponseEntity invite(@RequestParam(name = RequestKeys.NEW_MEMBER) long userId,
                                 @PathVariable(name = RequestKeys.GROUP_ID) long groupId) throws OperationException {
        groupMemberService.invite(userId, groupId);
        return ResponseHandler.responseSuccessful();
    }

    @PatchMapping("/acceptInvite")
    public ResponseEntity acceptInvite(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
                                       @PathVariable(name = RequestKeys.GROUP_ID) long groupId) throws OperationException, NotFoundException {
        groupMemberService.acceptInvite(userId, groupId);
        return ResponseHandler.responseSuccessful();
    }

    @PatchMapping("/rejectInvite")
    public ResponseEntity rejectInvite(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
                                       @PathVariable(name = RequestKeys.GROUP_ID) long groupId) throws OperationException, NotFoundException {
        groupMemberService.rejectInvite(userId, groupId);
        return ResponseHandler.responseSuccessful();
    }


    @PatchMapping("/kickout")
    public ResponseEntity kickout(@RequestParam(name = RequestKeys.KICKOUT) long userId,
                                  @PathVariable(name = RequestKeys.GROUP_ID) long groupId) throws OperationException, NotFoundException {
        groupMemberService.kickout(userId, groupId);
        return ResponseHandler.responseSuccessful();
    }

    @PatchMapping("/exit")
    public ResponseEntity exit(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
                               @RequestAttribute(name = AttributeKeys.GROUP) GroupDTO group) throws OperationException {
        groupMemberService.exit(userId, group);
        return ResponseHandler.responseSuccessful();
    }
}
