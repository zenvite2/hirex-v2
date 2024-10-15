package com.ptit.websocket.service.impl;

import com.ptit.data.entity.Message;
import com.ptit.data.entity.User;
import com.ptit.data.repository.MessageRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.websocket.dto.MessageDto;
import com.ptit.websocket.dto.UserConversationsDto;
import com.ptit.websocket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setType(messageDto.getType());
        message.setStatus(messageDto.getStatus());
        message.setSentTime(messageDto.getSentTime());
        message.setSenderId(Long.valueOf(messageDto.getSender()));
        message.setReceiverId(Long.valueOf(messageDto.getReceiver()));

        messageRepository.save(message);
    }

    @Override
    public List<UserConversationsDto> getUserConversations(Long currentUserId) {
        List<Message> messages = messageRepository.findTop10BySenderIdOrReceiverIdOrderBySentTimeDesc(currentUserId, currentUserId);

        return messages.stream()
                .collect(Collectors.groupingBy(message -> message.getSenderId() == currentUserId ? message.getReceiverId() : message.getSenderId()))
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
                                    message.getType()
                            ))
                            .collect(Collectors.toList());

                    User otherUser = userRepository.findById(otherUserId).orElseThrow(RuntimeException::new);
                    String name = otherUser.getUsername();
                    String avatar = otherUser.getAvtUrl();

                    return new UserConversationsDto(otherUserId, name, avatar, last10Messages);
                })
                .collect(Collectors.toList());
    }
}
