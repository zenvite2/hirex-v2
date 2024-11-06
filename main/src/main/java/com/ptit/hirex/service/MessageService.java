package com.ptit.hirex.service;

import com.ptit.hirex.dto.UserConversationsDto;

import java.util.List;

public interface MessageService {
    List<UserConversationsDto> getUserConversations(Long currentUserId);
}
