package com.ptit.hirex.controller;

import com.ptit.data.entity.NotificationPattern;
import com.ptit.hirex.dto.request.NotificationPatternRequest;
import com.ptit.hirex.service.NotificationPatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification-patterns")
public class NotificationPatternController {

    private final NotificationPatternService service;

    @PostMapping("/create")
    public NotificationPattern createNotificationPattern(@RequestBody NotificationPatternRequest request) {
        return service.save(request);
    }
}
