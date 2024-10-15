package com.ptit.websocket.controller;

import com.ptit.data.enums.Status;
import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.dto.UserConversationsDto;
import com.ptit.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@CrossOrigin
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
        if (messageDto.getStatus() != Status.JOIN) {
            chatService.saveMessage(messageDto);
        }
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(messageDto.getReceiver()), "/private", messageDto);
    }

    @ResponseBody
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<UserConversationsDto>> getUserConversations(@PathVariable Long userId) {
        List<UserConversationsDto> conversations = chatService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}