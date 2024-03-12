package com.example.demo.repository.entity;

import com.example.demo.controller.util.ColumnNames;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GroupMemberId implements Serializable {
	
	@Column(name = ColumnNames.USER_ID)
	long userId;

	@Column(name = ColumnNames.GROUP_ID)
	long groupId;
	
	public GroupMemberId() {
	}

	public GroupMemberId(long userId, long groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}
}
