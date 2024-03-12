package com.example.demo.service;

import com.example.demo.controller.util.RegexType;
import com.example.demo.exception.*;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.entity.UserEntity;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public void validateUserExistence(long userId) throws NotFoundException {
		if(!userRepository.existsById(userId)){
			throw new NotFoundException("user not found");
		}
	}

	@Transactional(readOnly = true)
	public boolean isAccountExists(String account) {
		return userRepository.existsByAccount(account);
	}

	@Transactional(readOnly = true)
	public UserDTO getUserDTO(long userId) {
		UserEntity user = userRepository.getById(userId);
		return UserDTO.createByEntity(user);
	}

	@Transactional
	public UserDTO register(String account, String password, String username) throws FormatException, DuplicateException {
		validateFormat(account, password, username);
		if(isAccountExists(account)){
			throw new DuplicateException("account already exists");
		}
		return createUser(account, password, username);
	}

	private UserDTO createUser(String account, String password, String username){
		UserEntity user = new UserEntity(account, password, username);
		userRepository.save(user);
		return UserDTO.createByEntity(user);
	}

	private void validateFormat(String account, String password, String username) throws FormatException, DuplicateException {
		RegexType.EMAIL.verifyAndThrow(account);
		RegexType.PASSWORD.verifyAndThrow(password);
		RegexType.verifyStringInRangeAndThrow(username, 30);
	}

	private UserEntity findUserEntityByAccount(String account) throws NotFoundException {
		return userRepository.findByAccount(account)
				.orElseThrow(() -> new NotFoundException("account not found"));
	}

	@Transactional(readOnly = true)
	public UserDTO login(String account, String password) throws LoginException, NotFoundException {
		UserEntity user = findUserEntityByAccount(account);
		validatePasswordCorrect(user, password);
		return UserDTO.createByEntity(user);
	}

	private void validatePasswordCorrect(UserEntity user, String password) throws LoginException {
		if(!user.isPasswordVerified(password)){
			throw new LoginException("password not matched");
		}
	}

	private void validateNewPassword(String password, String newPassword) throws OperationException, FormatException {
		RegexType.PASSWORD.verifyAndThrow(newPassword);
		if(password.equals(newPassword)){
			throw new OperationException("new password cannot be the same as old password");
		}
	}

	@Transactional
	public void changePassword(long userId, String password, String newPassword) throws LoginException, OperationException, FormatException {
		UserEntity user = userRepository.getById(userId);
		validatePasswordCorrect(user, password);
		validateNewPassword(password, newPassword);
		user.updatePassword(newPassword);
		userRepository.save(user);
	}

	@Transactional
	public void updateUsername(long userId, String username) {
		userRepository.updateUsernameById(userId, username);
	}

}