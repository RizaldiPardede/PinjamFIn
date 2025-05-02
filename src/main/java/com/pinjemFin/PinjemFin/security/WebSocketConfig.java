package com.pinjemFin.PinjemFin.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompJwtAuthChannelInterceptor stompJwtAuthChannelInterceptor;

    // Injeksi interceptor melalui konstruktor
    public WebSocketConfig(StompJwtAuthChannelInterceptor stompJwtAuthChannelInterceptor) {
        this.stompJwtAuthChannelInterceptor = stompJwtAuthChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Menambahkan endpoint WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Menentukan prefix untuk client subscribe dan send message
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Menambahkan interceptor pada inbound channel
        registration.interceptors(stompJwtAuthChannelInterceptor);
    }
}
