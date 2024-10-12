package com.ptit.websocket.service;

import com.ptit.websocket.dto.MessageDto;

public interface ChatService {
    void saveMessage(MessageDto messageDto);

}
