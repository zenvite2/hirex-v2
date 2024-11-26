package com.ptit.websocket.controller;

import com.ptit.data.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@CrossOrigin
@Slf4j
@Controller
public class NotificationController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(notification.getToUserId()), "/topic/notification", notification);
    }

    @PostMapping("/send-multiple-notification")
    public void sendMultipleNotification(@RequestBody List<Notification> notification) {
        notification.forEach(item -> simpMessagingTemplate.convertAndSendToUser(String.valueOf(item.getToUserId()), "/topic/notification", item));
    }
}
