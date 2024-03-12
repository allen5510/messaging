package com.example.demo.controller.DTO;

import com.example.demo.repository.entity.MessageEntity;
import com.example.demo.controller.util.ResponseKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MessageDTO {
    private long id;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.MESSAGE_SENDER_ID)))
    private long senderId;
    private String text;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.MESSAGE_SEND_AT)))
    private long sendAt;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.READ_COUNTS)))
    @Setter
    private int readCount = 0;

    public MessageDTO(MessageEntity message){
        this.id = message.getId();
        this.senderId = message.getSender();
        this.text = message.getText();
        this.sendAt = message.getSendAt();
        this.readCount = 0;
    }

    public static MessageDTO createByEntity(MessageEntity message) {
        return new MessageDTO(message);
    }

//    public static MessageDTO createByEntity(MessageEntity message, int readCount) {
//        MessageDTO messageDTO = new MessageDTO(message);
//        messageDTO.readCount = readCount;
//        return messageDTO;
//    }
}
