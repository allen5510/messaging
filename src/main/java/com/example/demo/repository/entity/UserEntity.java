package com.example.demo.repository.entity;

import com.example.demo.controller.util.ColumnNames;
import com.example.demo.util.HashGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity(name = "user")
@Getter
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ColumnNames.ID)
	private long id;

	@Column(name = ColumnNames.USER_ACCOUNT)
	private String account;

	@Setter
	@Column(name = ColumnNames.USER_USERNAME)
	private String username;

	@Basic(fetch=FetchType.LAZY)
	@Getter(AccessLevel.NONE)
	@Column(name = ColumnNames.USER_PASSWORD_HASH)
	private String passwdHash;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@Getter(onMethod = @__(@Transient))
	private List<GroupMemberEntity> userGroups = new ArrayList<>();
	
	public UserEntity() {
	}

	public UserEntity(String account, String password, String username) {
		this.account = account;
		this.username = username;
		this.passwdHash = HashGenerator.generateSHA256Hash(password);
	}

	public boolean isPasswordVerified(String password) {
		String passwordHash = HashGenerator.generateSHA256Hash(password);
		return this.passwdHash.equals(passwordHash);
	}

	public void updatePassword(String password) {
		this.passwdHash = HashGenerator.generateSHA256Hash(password);
	}

}
