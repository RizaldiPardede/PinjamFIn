package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.TokenNotifikasi;
import com.pinjemFin.PinjemFin.repository.CustomerRepository;
import com.pinjemFin.PinjemFin.repository.TokenNotifikasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenNotifikasiService {
    @Autowired
    private TokenNotifikasiRepository tokenNotifikasiRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NotificationService notificationService;

    public TokenNotifikasi addToken(String tokenNotif, UUID idCustomer) {
        TokenNotifikasi tokenNotifikasi = new TokenNotifikasi();
        tokenNotifikasi.setToken(tokenNotif);

        if (idCustomer != null) {
            customerRepository.findById(idCustomer).ifPresent(tokenNotifikasi::setUsersCustomer);
            // Kalau idCustomer tidak ditemukan, usersCustomer tetap null
        }

        return tokenNotifikasiRepository.save(tokenNotifikasi);


    }

    public TokenNotifikasi clearUserCustomerByToken(String tokenNotif) {
        Optional<TokenNotifikasi> optionalToken = tokenNotifikasiRepository.findByToken(tokenNotif);

        if (optionalToken.isPresent()) {
            TokenNotifikasi tokenNotifikasi = optionalToken.get();
            tokenNotifikasi.setUsersCustomer(null);
            return tokenNotifikasiRepository.save(tokenNotifikasi);
        } else {
            throw new RuntimeException("Token not found");
        }
    }

    public void sendNotificationToTokens(List<TokenNotifikasi> tokens, String title, String body) {
        for (TokenNotifikasi tokenEntity : tokens) {
            String token = tokenEntity.getToken();
            if (token != null && !token.isEmpty()) {
                String response = notificationService.sendNotification(token, title, body);
                System.out.println("Sent to token: " + token + " | Response: " + response);
            }
        }
    }

}
