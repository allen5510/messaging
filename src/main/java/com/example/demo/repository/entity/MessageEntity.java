package com.example.demo.repository.entity;


import com.example.demo.controller.util.ColumnNames;
import lombok.Getter;

import javax.persistence.*;
import java.beans.Transient;

@Entity(name = "message")
@Getter
public class MessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ColumnNames.ID)
	private long id;

	@Column(name = ColumnNames.MESSAGE_SENDER_ID)
	private long sender;

	@Column(name = ColumnNames.GROUP_ID)
	private long groupId;

	@Column(name = ColumnNames.MESSAGE_TEXT)
	private String text;

	@Column(name = ColumnNames.MESSAGE_SEND_AT)
	private long sendAt;

	@MapsId(ColumnNames.GROUP_ID)
	@ManyToOne
	@JoinColumn(name = ColumnNames.GROUP_ID)
	@Getter(onMethod_=@__(@Transient))
	private GroupEntity group;

	public MessageEntity(){}

	public MessageEntity(long sender, long groupsno, String text) {
		this.sender = sender;
		this.groupId = groupsno;
		this.text = text;
		this.sendAt = System.currentTimeMillis();
	}

	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
}

