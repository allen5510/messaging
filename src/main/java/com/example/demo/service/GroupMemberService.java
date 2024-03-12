package com.example.demo.service;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.controller.DTO.GroupMemberDTO;
import com.example.demo.controller.DTO.JoinedGroupDTO;
import com.example.demo.exception.OperationException;
import com.example.demo.repository.entity.GroupMemberEntity;
import com.example.demo.repository.entity.GroupMemberId;
import com.example.demo.repository.entity.type.JoinStatus;
import com.example.demo.repository.GroupMemberRepository;
import com.example.demo.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Autowired
    public GroupMemberService(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    private Optional<GroupMemberEntity> findGroupMemberEntityOptional(long userId, long groupId){
        return groupMemberRepository.findById(new GroupMemberId(userId, groupId));
    }

    public GroupMemberDTO getGroupMemberDTO(long userId, long groupId) {
        GroupMemberEntity memberEntity = groupMemberRepository.getById(new GroupMemberId(userId, groupId));
        return GroupMemberDTO.createByEntity(memberEntity);
    }

    public void createGroupMember(long userId, long groupId, JoinStatus status) {
        groupMemberRepository.createGroupMember(userId, groupId, status.rawValue(), System.currentTimeMillis());
    }

    public void updateMemberStatus(long userId, long groupId, JoinStatus status) {
        groupMemberRepository.updateStatus(userId, groupId, status, System.currentTimeMillis());
    }

    @Transactional
    public void updateMemberReadTimestamp(long userId, long groupId) {
        groupMemberRepository.updateReadTimestamp(userId, groupId, System.currentTimeMillis());
    }

    @Transactional(readOnly = true)
    public Set<GroupMemberDTO> queryMembersByGroupId(long groupId) {
        Set<GroupMemberEntity> members = groupMemberRepository.queryMembersByGroupId(groupId);
        return members.stream()
                .map(member -> GroupMemberDTO.createByEntity(member))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<JoinedGroupDTO> queryGroupsOfUser(long userId, long updatedAfter) {
        List<GroupMemberEntity> members = groupMemberRepository
                .queryGroupsByUserId(userId, updatedAfter, Pageable.ofSize(Const.QUERY_LIMIT));

        return members.stream()
                .map(groupMember -> JoinedGroupDTO.createByEntity(groupMember))
                .collect(Collectors.toList());
    }

    @Transactional
    public void invite(long userId, long groupId) throws OperationException {
        validateGroupMembershipLimit(groupId);
        Optional<GroupMemberEntity> groupMemberOpt = findGroupMemberEntityOptional(userId, groupId);
        if(groupMemberOpt.isPresent()){
            JoinStatus status = groupMemberOpt.get().getJoinStatus();
            validateInvitationStatus(status);
            updateMemberStatus(userId, groupId, JoinStatus.WAIT);
        } else {
            createGroupMember(userId, groupId, JoinStatus.WAIT);
        }
    }

    private void validateGroupMembershipLimit(long groupId) throws OperationException {
        int count = groupMemberRepository.countMembersByGroupId(groupId);
        if(count >= Const.GROUP_MEMBER_LIMIT){
            throw new OperationException("group member limit reached");
        }
    }

    private void validateInvitationStatus(JoinStatus currentStatus) throws OperationException {
        if(currentStatus == JoinStatus.JOIN || currentStatus == JoinStatus.WAIT) {
            throw new OperationException("User has already joined or been invited.");
        }
    }

    private void validateHaveInvited(long userId, long groupId) throws OperationException {
        GroupMemberEntity member = findGroupMemberEntityOptional(userId, groupId)
                                            .orElseThrow(() -> new OperationException("no invitation received"));
        JoinStatus status = member.getJoinStatus();
        if(!status.isWait()){
            throw new OperationException("no invitation received");
        }
    }

    @Transactional
    public void acceptInvite(long userId, long groupId) throws OperationException {
        validateHaveInvited(userId, groupId);
        groupMemberRepository.setStatusJoined(userId, groupId, System.currentTimeMillis());
    }

    @Transactional
    public void rejectInvite(long userId, long groupId) throws OperationException {
        validateHaveInvited(userId, groupId);
        updateMemberStatus(userId, groupId, JoinStatus.EXIT);
    }


    @Transactional(readOnly = true)
    public void validateGroupMembership(long userId, long groupId) throws OperationException {
        if(!isGroupMembership(userId, groupId)){
            throw new OperationException("user is not in group");
        }
    }

    private boolean isGroupMembership(long userId, long groupId) {
        return findGroupMemberEntityOptional(userId, groupId)
                .map(GroupMemberEntity::getJoinStatus)
                .map(JoinStatus::isJoined)
                .orElse(false);
    }

    @Transactional
    public void kickout(long kickoutUserId, long groupId) throws OperationException {
        validateGroupMembership(kickoutUserId, groupId);
        updateMemberStatus(kickoutUserId, groupId, JoinStatus.EXIT);
    }

    @Transactional
    public void exit(long userId, GroupDTO group) throws OperationException {
        if(group.getOwnerId() == userId){
            throw new OperationException("group owner cannot leave the group");
        }
        updateMemberStatus(userId, group.getId(), JoinStatus.EXIT);
    }

}
