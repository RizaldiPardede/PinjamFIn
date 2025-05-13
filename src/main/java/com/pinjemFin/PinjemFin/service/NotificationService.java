package com.pinjemFin.PinjemFin.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String sendNotification(String token, String title, String body) {
        try {
            // Membuat pesan yang akan dikirim ke perangkat Android
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            // Mengirim pesan menggunakan FCM
            String response = FirebaseMessaging.getInstance().send(message);
            return response; // Response berisi ID pesan yang berhasil dikirim

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Error sending notification";
            }
    }
}
