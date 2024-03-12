package com.example.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.controller.DTO.GroupMemberDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.util.Const;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.controller.DTO.MessageDTO;
import com.example.demo.controller.DTO.MessageReadCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.entity.MessageEntity;
import com.example.demo.repository.MessageRepository;

@Service
public class MessageService {
	private final MessageRepository messageRepository;

	private final GroupMemberService groupMemberService;

	@Autowired
	public MessageService(MessageRepository messageRepository, GroupMemberService groupMemberService) {
		this.messageRepository = messageRepository;
		this.groupMemberService = groupMemberService;
	}
	
	@Transactional
	public MessageDTO sendMessage(long senderId, long groupId, String text) throws NotFoundException {
		MessageEntity message = createMessage(senderId, groupId, text);
		groupMemberService.updateMemberReadTimestamp(senderId, groupId);
		return MessageDTO.createByEntity(message);
	}

	private MessageEntity createMessage(long senderId, long groupId, String text){
		MessageEntity message = new MessageEntity(senderId, groupId, text);
		messageRepository.save(message);
		return message;
	}

	@Transactional(readOnly = true)
	public List<MessageDTO> queryMessagesDTO(long userId, long groupId, long timestamp, boolean backward) {
		GroupMemberDTO member = groupMemberService.getGroupMemberDTO(userId, groupId);
		Set<GroupMemberDTO> members = groupMemberService.queryMembersByGroupId(groupId);
		List<MessageEntity> messages = queryMessagesEntity(groupId, member.getJoinedAt(), timestamp, backward);
		List<MessageDTO> messagesDTO = mappingToMessageDTO(userId, messages, members);
		return messagesDTO;
	}

	private List<MessageDTO> mappingToMessageDTO(long userId, List<MessageEntity> messages, Set<GroupMemberDTO> members){
		List<MessageDTO> messagesDTO = messages.stream().map(message -> {
			int count = getMessageReadCount(userId, message, members);
			MessageDTO messageDTO = MessageDTO.createByEntity(message);
			messageDTO.setReadCount(count);
			return messageDTO;
		}).collect(Collectors.toList());
		return messagesDTO;
	}

	private int getMessageReadCount(long userId, MessageEntity message, Set<GroupMemberDTO> members){
		int count = (int) members.stream()
				.filter(member->member.getId() != userId && // 排除自己
								member.getId() != message.getSender() && // 排除 sender
								member.getJoinedAt() < message.getSendAt() &&
								member.getReadAt() > message.getSendAt())
				.count();
		return count;
	}

	private List<MessageEntity> queryMessagesEntity(long groupId, long joinedTimestamp, long queryTimestamp, boolean queryBackward){
		List<MessageEntity> messages = queryBackward ?
				messageRepository.queryOlderInRangeMessagesByGroupId(groupId, joinedTimestamp, queryTimestamp, Pageable.ofSize(Const.QUERY_LIMIT)):
				messageRepository.queryNewerInRangeMessagesByGroupId(groupId, joinedTimestamp, queryTimestamp, Pageable.ofSize(Const.QUERY_LIMIT));
		return messages;
	}

	@Transactional(readOnly = true)
	public List<MessageReadCountDTO> queryMessageReadCount(long userId, long groupId, long timestamp) {
		List<MessageDTO> messages = queryMessagesDTO(userId, groupId, timestamp, false);
		List<MessageReadCountDTO> readCounts = messages.stream()
														.map(message->new MessageReadCountDTO(message.getId(), message.getReadCount()))
														.collect(Collectors.toList());
		return readCounts;
	}

}
