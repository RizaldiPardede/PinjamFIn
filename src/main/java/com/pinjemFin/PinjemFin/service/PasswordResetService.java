package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.PasswordResetToken;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.PasswordResetTokenRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UsersRepository usersRepository;



    public void sendResetPasswordEmail(Users users) {
        String token = UUID.randomUUID().toString();

        // Cek apakah user sudah punya token
        Optional<PasswordResetToken> existingTokenOpt = passwordResetTokenRepository.findByUsers(users);

        PasswordResetToken resetToken;

        if (existingTokenOpt.isPresent()) {
            // Update token & expiry date
            resetToken = existingTokenOpt.get();
            resetToken.setToken(token);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        } else {
            // Buat token baru
            resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUsers(users);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        }
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://35.202.72.129/resetpassword/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(users.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("Click this link to reset your password: " + resetLink);

        mailSender.send(message);
    }

    public ResponseEntity <Map<String, String>>handleForgotPassword(String email) {
        Users users = usersRepository.findByEmail(email).get();
        if (users == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email tidak ditemukan");
            return ResponseEntity.badRequest().body(response);
        }
        sendResetPasswordEmail(users);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email reset password telah dikirim");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> handleShowResetForm(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        Map<String, String> response = new HashMap<>();

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            response.put("message", "Token tidak valid atau kedaluwarsa");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("message", "Silakan masukkan password baru");
        response.put("token", resetToken.getToken());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity <Map<String, String>> handleResetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        Map<String, String> response = new HashMap<>();

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            response.put("message", "Token tidak valid atau kedaluwarsa");
            return ResponseEntity.badRequest().body(response);
        }

        Users users = resetToken.getUsers();
        // Langsung pakai BCrypt tanpa dependency injection
        users.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        usersRepository.save(users);

        passwordResetTokenRepository.delete(resetToken);
        response.put("message", "Password berhasil diubah");
        return ResponseEntity.ok(response);
    }



}
