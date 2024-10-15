package com.ptit.websocket.service;

import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.dto.UserConversationsDto;

import java.util.List;

public interface ChatService {
    void saveMessage(MessageDto messageDto);

    List<UserConversationsDto> getUserConversations(Long currentUserId);
}
