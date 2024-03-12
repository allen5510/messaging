package com.example.demo.controller.DTO;

import com.example.demo.controller.util.ResponseKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageReadCountDTO {

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.MESSAGE_ID)))
    private long messageId;

    @Getter(onMethod_=@__(@JsonProperty(ResponseKeys.READ_COUNT)))
    private long readCount;

    public MessageReadCountDTO(long id, long readCount){
        this.messageId = id;
        this.readCount = readCount;
    }
}
