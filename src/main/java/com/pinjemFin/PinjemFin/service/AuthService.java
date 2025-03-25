package com.pinjemFin.PinjemFin.services;

import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateUser(String email, String password) {
        logger.info("Mencoba login dengan email: {}", email);  // Cek apakah email diterima

        Optional<Users> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            logger.info("User ditemukan: {}", user.getEmail()); // Konfirmasi user ditemukan di database
            // Langsung bandingkan dengan password di database (karena belum di-hash)
            if (user.getPassword().equals(password)) {
                logger.info("Password cocok, menghasilkan token...");
                return jwtUtil.generateToken(email); // Kembalikan JWT Token
            }
            else {
                logger.warn("Password tidak cocok untuk email: {}", email);
            }
        }
        else {
            logger.warn("User dengan email {} tidak ditemukan di database", email);
        }
        return null; // Jika gagal login
    }
}
