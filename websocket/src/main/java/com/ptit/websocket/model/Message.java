package com.ptit.websocket.model;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Message {
    private String sender;
    private String receiver;
    private String message;
    private Long sendAt;
    private Status status;
}