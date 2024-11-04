package com.ptit.websocket.service;

import com.ptit.websocket.dto.MessageDto;

public interface MessageService {
    void saveMessage(MessageDto messageDto);
}
