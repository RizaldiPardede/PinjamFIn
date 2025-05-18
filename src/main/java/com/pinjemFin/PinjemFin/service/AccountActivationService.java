package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.ActivationToken;
import com.pinjemFin.PinjemFin.models.PasswordResetToken;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.ActivationTokenRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountActivationService {
    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private UsersRepository usersRepository;

    public String ActivationEmail(String tokenActivation) {
        ActivationToken activationToken = activationTokenRepository.findBytokenActivation(tokenActivation);

        if (activationToken.getTokenActivation() == null) {
            return "Invalid Token";
        }
        else {
            Users users = activationToken.getUsers();
            users.setIsActive(true);
            usersRepository.save(users);
            activationTokenRepository.delete(activationToken);
            return "Activation Email Success";
        }

    }

    public String createTokenActivation(Users users) {

        String token = UUID.randomUUID().toString();

        Optional<ActivationToken> existingTokenOpt = activationTokenRepository.findByUsers(users);

        ActivationToken activationToken;

        if (existingTokenOpt.isPresent()) {
            // Update token & expiry date
            activationToken = existingTokenOpt.get();
            activationToken.setTokenActivation(token);
            activationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        } else {
            // Buat token baru
            activationToken = new ActivationToken();
            activationToken.setTokenActivation(token);
            activationToken.setUsers(users);
            activationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        }
        activationTokenRepository.save(activationToken);


        return token;
    }
}
