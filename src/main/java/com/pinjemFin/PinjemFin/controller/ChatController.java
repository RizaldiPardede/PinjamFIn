package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send") // client kirim ke: /app/chat/send
    public void sendMessage(ChatMessage message) {
        String destination = "/queue/messages/" + message.getReceiverNip(); // receiver listen di sini
        messagingTemplate.convertAndSend(destination, message);
    }
}