package com.example.demo.repository.entity;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.example.demo.controller.util.ColumnNames;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "group_entity")
@Getter
public class GroupEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ColumnNames.ID)
	private long id;

	@Setter
	@Column(name = ColumnNames.GROUP_OWNER_ID)
	private long ownerId;

	@Column(name = ColumnNames.GROUP_NAME)
	private String name;

	@Column(name = ColumnNames.UPDATED_AT)
	@Setter
	private long updatedAt;

	@OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@Getter(onMethod_=@__(@Transient))
	private Set<GroupMemberEntity> groupMembers = new HashSet<>();
		
	@OneToMany(mappedBy = "group",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@Getter(onMethod_=@__(@Transient))
	private List<MessageEntity> messages = new ArrayList<>();

	GroupEntity(){} // 不能刪

	public GroupEntity(String name, long ownerId) {
		this.name = name;
		this.ownerId = ownerId;
		this.updateTimestamp();
	}

	public void addMessage(MessageEntity message) {
		message.setGroup(this);
		this.messages.add(message);
		this.updateTimestamp();
	}

	public void updateTimestamp() {
		this.updatedAt = System.currentTimeMillis();
	}

	public void addGroupMember(GroupMemberEntity userGroup) {
		this.groupMembers.add(userGroup);
	}

}
