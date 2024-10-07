package com.ptit.websocket.controller;

import com.ptit.websocket.model.Message;
import com.ptit.websocket.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "WS IS RUNNING.";
    }

    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload Message message) {
        log.info("Private message from {} to {}: {}", message.getSender(), message.getReceiver(), message.getMessage());
        // TODO: luu vao db
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
    }
}