package com.ptit.hirex.service.impl;

import com.ptit.data.entity.Notification;
import com.ptit.data.entity.NotificationPattern;
import com.ptit.data.repository.NotificationPatternRepository;
import com.ptit.data.repository.NotificationRepository;
import com.ptit.hirex.dto.request.NotificationRequest;
import com.ptit.hirex.service.MailService;
import com.ptit.hirex.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final ModelMapper modelMapper;
    private final NotificationRepository repository;
    private final NotificationPatternRepository patternRepository;
    private final MailService mailService;

    public void save(NotificationRequest req) throws MessagingException {
        Notification notification = repository.save(modelMapper.map(req, Notification.class));
        NotificationPattern notificationPattern = patternRepository.findById(notification.getPatternId()).orElse(null);
        if (notificationPattern != null) {
            if (notificationPattern.getIsHtml()) {
                mailService.sendHtmlEmail(String.valueOf(notification.getToUserId()), notificationPattern.getSubject(), notificationPattern.getContent());
            } else
                mailService.sendMail(String.valueOf(notification.getToUserId()), notificationPattern.getSubject(), notificationPattern.getContent());
        }
        throw new MessagingException("Error when sending mail.");
    }
}
