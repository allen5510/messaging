package com.example.demo.service;

import com.example.demo.controller.util.RegexType;
import com.example.demo.exception.FormatException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.OperationException;
import com.example.demo.repository.entity.GroupEntity;
import com.example.demo.util.Const;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.repository.entity.type.JoinStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.GroupRepository;

@Service
public class GroupService {
	private final GroupRepository groupRepository;
	private final GroupMemberService groupMemberService;

	@Autowired
	public GroupService(GroupRepository groupRepository, GroupMemberService groupMemberService) {
		this.groupRepository = groupRepository;
		this.groupMemberService = groupMemberService;
	}

	@Transactional(readOnly = true)
	public GroupDTO findGroupDTO(long groupId) throws NotFoundException {
		GroupEntity group = groupRepository.findById(groupId)
											.orElseThrow(() -> new NotFoundException("group not found"));
		return GroupDTO.createByEntity(group);
	}

	@Transactional
	public GroupDTO createGroup(String name, long ownerId) throws FormatException {
		RegexType.verifyStringInRangeAndThrow(name, Const.GROUP_NAME_SIZE_LIMIT);

		GroupEntity group = new GroupEntity(name, ownerId);
		groupRepository.save(group);
		groupMemberService.createGroupMember(ownerId, group.getId(), JoinStatus.JOIN);
		return GroupDTO.createByEntity(group);
	}

	public void validateGroupAdmin(long userId, GroupDTO group) throws OperationException {
		if(group.getOwnerId() != userId){
			throw new OperationException("user is not group owner");
		}
	}

	@Transactional
	public void updateGroupOwner(long userId, long newOwnerId, GroupDTO group) throws OperationException {
		validateGroupAdmin(userId, group);
		groupMemberService.validateGroupMembership(newOwnerId, group.getId());
		groupRepository.updateOwnerById(group.getId(), newOwnerId);
	}

	@Transactional
	public void deleteGroup(long userId, GroupDTO group) throws OperationException {
		validateGroupAdmin(userId, group);
		groupRepository.deleteById(group.getId());
	}
}