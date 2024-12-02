package com.ptit.hirex.service;

import com.ptit.data.entity.NotificationPattern;
import com.ptit.hirex.dto.request.NotificationPatternRequest;

import java.util.List;

public interface NotificationPatternService {
    NotificationPattern save(NotificationPatternRequest notificationPattern);

}
