package com.example.demo.controller.DTO;

import com.example.demo.repository.entity.GroupMemberEntity;
import com.example.demo.controller.util.ResponseKeys;
import com.example.demo.repository.entity.type.JoinStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class GroupMemberDTO extends UserDTO {
    private JoinStatus joinStatus;

    @Getter(onMethod_=@__(@JsonIgnore))
    private long readAt;

    @Getter(onMethod_=@__(@JsonIgnore))
    private long joinedAt;

    @Getter(onMethod = @__(@JsonProperty(ResponseKeys.UPDATED_AT)))
    private long updatedAt;

    private GroupMemberDTO(GroupMemberEntity groupMember) {
        super(groupMember.getUser());
        this.joinStatus = groupMember.getJoinStatus();
        this.readAt = groupMember.getReadAt();
        this.joinedAt = groupMember.getJoinedAt();
        this.updatedAt = groupMember.getUpdatedAt();
    }

    public static GroupMemberDTO createByEntity(GroupMemberEntity groupMember) {
        return new GroupMemberDTO(groupMember);
    }

    @JsonProperty(ResponseKeys.JOIN_STATUS)
    public int getJoinStatusRawValue() {
        return joinStatus.rawValue();
    }
}
