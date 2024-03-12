package com.example.demo.controller;

import java.util.List;

import com.example.demo.controller.DTO.MessageDTO;
import com.example.demo.controller.DTO.MessageReadCountDTO;
import com.example.demo.controller.util.RequestKeys;
import com.example.demo.controller.util.AttributeKeys;
import com.example.demo.controller.util.ResponseHandler;
import com.example.demo.controller.util.ResponseKeys;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/groups/{"+RequestKeys.GROUP_ID+"}/messages")
public class MessageController {

	private final MessageService messageService;

	private final GroupMemberService groupMemberService;

	@Autowired
	public MessageController(MessageService messageService, GroupMemberService groupMemberService) {
		this.messageService = messageService;
		this.groupMemberService = groupMemberService;
	}

	@GetMapping
	public ResponseEntity query(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
								@PathVariable(name = RequestKeys.GROUP_ID) long groupId,
								@RequestParam(name = RequestKeys.TIMESTAMP, defaultValue = "0") long timestamp,
								@RequestParam(name = RequestKeys.BACKWARD, defaultValue = "false") boolean backward){
		List<MessageDTO> messages = messageService.queryMessagesDTO(userId, groupId, timestamp, backward);
		return ResponseHandler.responseQueryResults(ResponseKeys.MESSAGES, messages);
	}

	@PostMapping
	public ResponseEntity send(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
							   @PathVariable(name = RequestKeys.GROUP_ID) long groupId,
							   @RequestParam(name = RequestKeys.TEXT) String text) throws NotFoundException {
		MessageDTO messageDTO = messageService.sendMessage(userId, groupId, text);
		return ResponseHandler.responseCreatedSuccessful(ResponseKeys.MESSAGE, messageDTO);
	}

	@PatchMapping("/read")
	public ResponseEntity markRead(@PathVariable(name = RequestKeys.GROUP_ID) long groupId,
								   @RequestAttribute(name = AttributeKeys.USER_ID) long userId) throws NotFoundException {
		groupMemberService.updateMemberReadTimestamp(userId, groupId);
		return ResponseHandler.responseSuccessful();
	}

	@GetMapping("/read")
	public ResponseEntity readCount(@RequestAttribute(name = AttributeKeys.USER_ID) long userId,
									@PathVariable(name = RequestKeys.GROUP_ID) long groupId,
									@RequestParam(name = RequestKeys.TIMESTAMP, defaultValue = "0") long timestamp){
		List<MessageReadCountDTO> readCounts = messageService.queryMessageReadCount(userId, groupId, timestamp);
		return ResponseHandler.responseQueryResults(ResponseKeys.READ_COUNTS, readCounts);
	}

}
