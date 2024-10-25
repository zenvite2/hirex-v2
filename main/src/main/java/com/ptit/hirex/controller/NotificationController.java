package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.NotificationRequest;
import com.ptit.hirex.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/create")
    public void createNotification(@RequestBody NotificationRequest req) throws MessagingException {
        service.save(req);
    }
}
