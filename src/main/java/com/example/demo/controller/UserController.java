package com.example.demo.controller;

import com.example.demo.controller.DTO.UserDTO;
import com.example.demo.exception.*;
import com.example.demo.controller.util.RequestKeys;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.controller.util.RegexType;
import com.example.demo.controller.util.ResponseHandler;
import com.example.demo.controller.util.ResponseKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.UserService;
import com.example.demo.util.TokenProcessor;

@RestController
@RequestMapping("/account")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity register(@RequestParam(name = RequestKeys.ACCOUNT) String account,
								   @RequestParam(name = RequestKeys.PASSWORD) String password,
								   @RequestParam(name = RequestKeys.USERNAME) String username)
			         										throws TokenException, FormatException, DuplicateException {

		UserDTO userDTO = userService.register(account, password, username);
		String token = TokenProcessor.generateTokenAndThrow(userDTO);

		return ResponseHandler
				.putSuccessfulStatus()
				.put(ResponseKeys.USER, userDTO)
				.put(ResponseKeys.TOKEN, token)
				.build(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestParam(name = RequestKeys.ACCOUNT) String account,
								@RequestParam(name = RequestKeys.PASSWORD) String password)
																throws LoginException, TokenException, NotFoundException {

		UserDTO user = userService.login(account, password);
		String token = TokenProcessor.generateTokenAndThrow(user);
		return ResponseHandler
					.putSuccessfulStatus()
					.put(ResponseKeys.TOKEN, token)
					.put(ResponseKeys.USER, user)
					.build();
	}

	@PostMapping("/tokenLogin")
	public ResponseEntity tokenLogin(@RequestAttribute(name = AttributeKeys.USER_ID) long userId) {
		UserDTO user = userService.getUserDTO(userId);
		return ResponseHandler.responseSuccessful(ResponseKeys.USER, user);
	}

	@PostMapping("/changePassword")
	public ResponseEntity changePassword(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
										 @RequestParam(name = RequestKeys.PASSWORD) String password,
										 @RequestParam(name = RequestKeys.NEW_PASSWORD) String newPassword) throws FormatException, LoginException, OperationException {
		userService.changePassword(userId, password, newPassword);
		return ResponseHandler.responseSuccessful();
	}

	@PatchMapping("/update")
	public ResponseEntity update(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
								 @RequestParam(name = RequestKeys.USERNAME) String username) {
		userService.updateUsername(userId, username);
		return ResponseHandler.responseSuccessful();
	}
}
