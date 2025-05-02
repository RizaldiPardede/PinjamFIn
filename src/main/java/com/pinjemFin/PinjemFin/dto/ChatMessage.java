package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private String senderNip;
    private String receiverNip;
    private String content;
    private LocalDateTime timestamp;
}
