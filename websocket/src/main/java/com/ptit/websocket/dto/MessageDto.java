package com.ptit.websocket.dto;

import com.ptit.data.enums.MessageType;
import com.ptit.data.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class MessageDto {
    private UUID id;
    private String sender;
    private String receiver;
    private String message;
    private LocalDateTime sentTime;
    private Status status;
    private MessageType type;
    private String fileUrl;
}