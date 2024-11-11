package com.ptit.websocket.controller;

import com.ptit.data.enums.Status;
import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Slf4j
@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "WS IS RUNNING.";
    }

    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload MessageDto messageDto) {
        log.info("Private message from {} to {}: {}", messageDto.getSender(), messageDto.getReceiver(), messageDto.getMessage());
        if (messageDto.getStatus() != Status.JOIN) {
            messageService.saveMessage(messageDto);
        }
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(messageDto.getReceiver()), "/private", messageDto);
    }
}