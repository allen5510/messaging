package com.example.demo.controller.DTO;

import com.example.demo.controller.util.ResponseKeys;
import com.example.demo.repository.entity.GroupEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GroupDTO {
    private long id;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.GROUP_OWNER_ID)))
    private long ownerId;
    private String name;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.UPDATED_AT)))
    private long updatedAt;

    protected GroupDTO(GroupEntity group){
        this.id = group.getId();
        this.ownerId = group.getOwnerId();
        this.name = group.getName();
        this.updatedAt = group.getUpdatedAt();
    }

    public static  GroupDTO createByEntity(GroupEntity group){
        return new GroupDTO(group);
    }
}
