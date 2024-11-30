package com.ptit.hirex.service.impl;

import com.ptit.data.entity.Message;
import com.ptit.data.repository.MessageRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.MessageDto;
import com.ptit.hirex.dto.UserConversationsDto;
import com.ptit.hirex.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserConversationsDto> getUserConversations(Long userId) {
        List<Message> messages = messageRepository.findTop10BySenderIdOrReceiverIdOrderBySentTimeDesc(userId, userId);

        return messages.stream()
                .collect(Collectors.groupingBy(message -> message.getSenderId() == userId ? message.getReceiverId() : message.getSenderId()))
                .entrySet()
                .stream()
                .map(entry -> {
                    Long otherUserId = entry.getKey();
                    List<MessageDto> last10Messages = entry.getValue().stream()
                            .sorted(Comparator.comparing(Message::getSentTime))
                            .map(message -> new MessageDto(
                                    message.getId(),
                                    String.valueOf(message.getSenderId()),
                                    String.valueOf(message.getReceiverId()),
                                    message.getMessage(),
                                    message.getSentTime(),
                                    message.getStatus(),
                                    message.getType(),
                                    message.getFileUrl()
                            ))
                            .collect(Collectors.toList());

                    return userRepository.findById(otherUserId)
                            .map(otherUser -> {
                                String username = otherUser.getUsername();
                                String avatar = otherUser.getAvatar();
                                String fullName = otherUser.getFullName();
                                return new UserConversationsDto(otherUserId, username, fullName, avatar, last10Messages);
                            }).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
