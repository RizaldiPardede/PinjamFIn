package com.pinjemFin.PinjemFin.service;


import com.pinjemFin.PinjemFin.dto.RegisterRequest;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

            String storedPassword = user.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            boolean isPasswordMatch;

            // Cek apakah password sudah di-hash (BCrypt selalu diawali dengan "$2a$", "$2b$", atau "$2y$")
            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
                isPasswordMatch = passwordEncoder.matches(password, storedPassword);
            } else {
                // Password masih dalam bentuk plain text
                isPasswordMatch = storedPassword.equals(password);
            }

            if (isPasswordMatch) {
                logger.info("Password cocok, menghasilkan token...");

                // Jika password belum di-hash, lakukan hashing dan simpan kembali
                if (!storedPassword.startsWith("$2a$") && !storedPassword.startsWith("$2b$") && !storedPassword.startsWith("$2y$")) {
                    String hashedPassword = passwordEncoder.encode(password);
                    user.setPassword(hashedPassword);
                    usersRepository.save(user); // Simpan perubahan ke database
                    logger.info("Password telah di-hash dan diperbarui di database.");
                }

                return jwtUtil.generateToken(user); // Kembalikan JWT Token
            } else {
                logger.warn("Password tidak cocok untuk email: {}", email);
            }
        } else {
            logger.warn("User dengan email {} tidak ditemukan di database", email);
        }

        return null; // Jika gagal login
    }


    public Users registerUsers(RegisterRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users users = new Users();
        users.setEmail(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword())); //hasing password
        users.setNama(request.getNama());
        users.setRole(new Role("FE4F4E2B-C388-40BF-9C57-FE266A4729B1","customer"));

        return usersRepository.save(users);
    }
}
