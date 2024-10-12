package com.ptit.websocket.mapper;

import com.ptit.data.entity.Message;
import com.ptit.websocket.dto.MessageDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageDtoToMessage extends PropertyMap<MessageDto, Message> {

    @Override
    protected void configure() {
//        map().setMessage(source.getMessage());
//        map().setSenderId(Long.valueOf(source.getSender()));
//        map().setReceiverId(Long.valueOf(source.getReceiver()));
//        map().setStatus(source.getStatus());
//        map().setType(source.getType());
//        map().setSentTime(LocalDateTime.parse(source.getSentTime()));
    }
}

