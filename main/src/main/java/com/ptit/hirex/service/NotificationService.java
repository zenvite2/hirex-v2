package com.ptit.hirex.service;

import com.ptit.hirex.dto.request.NotificationRequest;
import jakarta.mail.MessagingException;

public interface NotificationService {

    void save(NotificationRequest req) throws MessagingException;
}
