package com.ptit.websocket.service.impl;

import com.ptit.data.entity.Message;
import com.ptit.data.repository.MessageRepository;
import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.service.ChatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MessageRepository messageRepository;

    @Override
    public void saveMessage(MessageDto messageDto) {
        Message message = modelMapper.map(messageDto, Message.class);
        messageRepository.save(message);
    }
}
