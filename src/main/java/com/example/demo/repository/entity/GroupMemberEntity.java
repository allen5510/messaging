package com.example.demo.repository.entity;

import com.example.demo.controller.util.ColumnNames;
import com.example.demo.repository.entity.type.JoinStatus;
import com.example.demo.repository.entity.type.JoinStatusConverter;
import lombok.Getter;

import java.beans.Transient;

import javax.persistence.*;

@Entity(name = "group_member")
@Getter
public class GroupMemberEntity {
	@EmbeddedId
	private GroupMemberId id = new GroupMemberId();

	@MapsId(ColumnNames.USER_ID)
	@ManyToOne
	@JoinColumn(name = ColumnNames.USER_ID)
	@Getter(onMethod_=@__(@Transient))
	private UserEntity user;

	@MapsId(ColumnNames.GROUP_ID)
	@ManyToOne // 必須實例化
	@JoinColumn(name = ColumnNames.GROUP_ID)
	@Getter(onMethod_=@__(@Transient))
	private GroupEntity group;

	@Convert(converter = JoinStatusConverter.class)
	@Column(name = ColumnNames.MEMBER_JOIN_STATUS)
	private JoinStatus joinStatus;

	@Column(name = ColumnNames.MEMBER_READ_AT)
	private long readAt = 0L;

	@Column(name = ColumnNames.MEMBER_JOINED_AT)
	private long joinedAt = 0L;

	@Column(name = ColumnNames.UPDATED_AT)
	private long updatedAt = 0L;

	public GroupMemberEntity() {
	}

//	public GroupMemberEntity(UserEntity user, GroupEntity group) {
//		this.group = group;
//		this.user = user;
//	}
//
//	public GroupMemberEntity(UserEntity user, GroupEntity group, JoinStatus status) {
//		this.group = group;
//		this.user = user;
//		this.joinStatus = status;
//	}
}
