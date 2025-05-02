package com.pinjemFin.PinjemFin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.stereotype.Component;


@Component
public class StompJwtAuthChannelInterceptor implements ChannelInterceptor {

    private JwtUtil jwtUtil;

    @Autowired
    public StompJwtAuthChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token == null) {
                String query = (String) accessor.getSessionAttributes().get("org.springframework.http.server.ServerHttpRequest.query");
                if (query != null && query.contains("token=")) {
                    token = query.substring(query.indexOf("token=") + 6);
                }
            }

            System.out.println("CONNECT Headers: " + accessor.toNativeHeaderMap());
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // hilangkan "Bearer "
                // Validasi token dan ekstrak email (atau username)
                String email = jwtUtil.extractEmailFromToken(token);
                if (jwtUtil.validateToken(token,email)) {
                    String username = jwtUtil.extractidUser(token);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, null); // bisa tambah authorities
                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    throw new IllegalArgumentException("Token JWT tidak valid!");
                }
            } else {
                throw new IllegalArgumentException("Token JWT tidak ditemukan di header Authorization!");
            }
        }

        return message;
    }
}
