package com.ptit.hirex.service.impl;

import com.ptit.data.entity.NotificationPattern;
import com.ptit.data.repository.NotificationPatternRepository;
import com.ptit.hirex.dto.request.NotificationPatternRequest;
import com.ptit.hirex.service.NotificationPatternService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationPatternServiceImpl implements NotificationPatternService {

    private final ModelMapper modelMapper;
    private final NotificationPatternRepository repository;

    public NotificationPattern save(NotificationPatternRequest notificationPattern) {
        return repository.save(modelMapper.map(notificationPattern, NotificationPattern.class));
    }

}
