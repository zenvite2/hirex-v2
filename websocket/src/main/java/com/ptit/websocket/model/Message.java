package com.ptit.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Message {
    private String sender;
    private String receiver;
    private String message;
    private String sentTime;
    private Status status;
    private MessageType type;
}