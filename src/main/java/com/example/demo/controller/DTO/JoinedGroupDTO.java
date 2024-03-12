package com.example.demo.controller.DTO;

import com.example.demo.repository.entity.GroupEntity;
import com.example.demo.repository.entity.GroupMemberEntity;
import com.example.demo.repository.entity.MessageEntity;
import com.example.demo.controller.util.ResponseKeys;
import com.example.demo.util.Const;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class JoinedGroupDTO extends GroupDTO {
    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.JOIN_STATUS)))
    private int joinStatusRawValue;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.UNREAD_COUNT)))
    private int unreadCount;

    private JoinedGroupDTO(GroupMemberEntity groupMember) {
        super(groupMember.getGroup());
        this.joinStatusRawValue = groupMember.getJoinStatus().rawValue();
        this.unreadCount = calculateGroupUnreadCount(groupMember);
    }

    public static JoinedGroupDTO createByEntity(GroupMemberEntity groupMember) {
        return new JoinedGroupDTO(groupMember);
    }

    private int calculateGroupUnreadCount(GroupMemberEntity groupMember){
        GroupEntity group = groupMember.getGroup();
        List<MessageEntity> messages = group.getMessages();
        long readTimestamp = groupMember.getReadAt();

        int unreadCount = (int) messages.stream()
                .filter(message -> message.getSendAt() > readTimestamp)
                .limit(Const.GROUP_UNREAD_MAX)
                .count();
        return unreadCount;
    }
}
