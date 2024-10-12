package com.ptit.websocket.dto;

import com.ptit.data.enums.MessageType;
import com.ptit.data.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class MessageDto {
    private String sender;
    private String receiver;
    private String message;
    private String sentTime;
    private Status status;
    private MessageType type;
}