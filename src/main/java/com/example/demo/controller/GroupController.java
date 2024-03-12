package com.example.demo.controller;

import java.util.List;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.controller.DTO.JoinedGroupDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.OperationException;
import com.example.demo.repository.entity.type.JoinStatus;
import com.example.demo.exception.FormatException;
import com.example.demo.controller.util.RequestKeys;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.controller.util.RegexType;
import com.example.demo.controller.util.ResponseHandler;
import com.example.demo.controller.util.ResponseKeys;
import com.example.demo.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {

	private final GroupService groupService;

	private final GroupMemberService groupMemberService;

	@Autowired
	public GroupController(GroupService groupService, GroupMemberService groupMemberService) {
		this.groupService = groupService;
		this.groupMemberService = groupMemberService;
	}

	@GetMapping
	public ResponseEntity queryGroups(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
									  @RequestParam(name=RequestKeys.UPDATE_AFTER, defaultValue = "0")long updateAfter) {
		List<JoinedGroupDTO> groups = groupMemberService.queryGroupsOfUser(userId, updateAfter);
		return ResponseHandler.responseQueryResults(ResponseKeys.GROUPS, groups);
	}

	@GetMapping("/{group_id}")
	public ResponseEntity queryGroup(@RequestAttribute(name = AttributeKeys.GROUP) GroupDTO group) {
		return ResponseHandler.responseSuccessful(ResponseKeys.GROUP, group);
	}

	@PostMapping
	public ResponseEntity create(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
								 @RequestParam(name = RequestKeys.NAME) String name) throws FormatException {
		GroupDTO group = groupService.createGroup(name, userId);
		return ResponseHandler.responseCreatedSuccessful(ResponseKeys.GROUP, group);
	}

	@PatchMapping("/{group_id}")
	public ResponseEntity appoint(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
								  @RequestAttribute(name = AttributeKeys.GROUP) GroupDTO group,
								  @RequestParam(name = RequestKeys.NEW_OWNER) long newOwnerId)
																		throws OperationException, NotFoundException {
		groupService.updateGroupOwner(userId, newOwnerId, group);
		return ResponseHandler.responseSuccessful();
	}

	@DeleteMapping("/{group_id}")
	public ResponseEntity delete(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
								 @RequestAttribute(name = AttributeKeys.GROUP) GroupDTO group)
																							throws OperationException {
		groupService.deleteGroup(userId, group);
		return ResponseHandler.responseSuccessful();
	}
}


