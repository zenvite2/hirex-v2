package com.ptit.websocket.controller;

import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "WS IS RUNNING.";
    }

    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload MessageDto messageDto) {
        log.info("Private message from {} to {}: {}", messageDto.getSender(), messageDto.getReceiver(), messageDto.getMessage());
        chatService.saveMessage(messageDto);
        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiver(), "/private", messageDto);
    }
}