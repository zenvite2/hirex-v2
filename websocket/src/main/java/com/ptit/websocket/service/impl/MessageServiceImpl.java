package com.ptit.websocket.service.impl;

import com.ptit.data.entity.Message;
import com.ptit.data.repository.MessageRepository;
import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Override
    public void saveMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setType(messageDto.getType());
        message.setStatus(messageDto.getStatus());
        message.setSentTime(messageDto.getSentTime());
        message.setSenderId(Long.valueOf(messageDto.getSender()));
        message.setReceiverId(Long.valueOf(messageDto.getReceiver()));
        message.setFileUrl(messageDto.getFileUrl());
        messageRepository.save(message);
    }
}
