package com.pinjemFin.PinjemFin.service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.pinjemFin.PinjemFin.dto.RegisterRequest;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.CustomException;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AccountActivationService accountActivationService;

    public String authenticateUser(String email, String password) {
        logger.info("Mencoba login dengan email: {}", email);  // Cek apakah email diterima

        Optional<Users> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            if (userOptional.get().getIsActive() || !userOptional.get().getRole().getNama_role().equals("customer")) {
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
                    return ("Password tidak cocok untuk email: "+ email);
                }
            }
            else{
                return ("Email belum di verifikasi segera cek emailmu untuk aktivasi"+ email);
            }

        } else {
            return "User dengan email {} tidak ditemukan di database" + email;
        }

    }


    public Users registerUsers(RegisterRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users users = new Users();
        users.setEmail(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword())); //hasing password
        users.setNama(request.getNama());
        users.setIsActive(false);
        users.setRole(roleRepository.findByNamaRole("customer"));

        try {
            String token = accountActivationService.createTokenActivation(usersRepository.save(users));
            String subject = "Verifikasi Email";
            String body = "Silahkan verifikasi email dengan mengklik link dibawah ini\n" +
                    "https://pinjemfin-verification.vercel.app/verifikasi?token="+token;
            emailService.sendEmail(users.getEmail(),subject,body);
            return users;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("UK6dotkott2kjsp8vw4d0m25fb7")) {
                throw new CustomException("Email telah digunakan");
            }
            throw e; // lempar error lain jika bukan karena duplikat email
        }
    }

    public Users registerUsersAuthGoogle(RegisterRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users users = new Users();
        users.setEmail(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword())); //hasing password
        users.setNama(request.getNama());
        users.setIsActive(true);

        Role role = new Role();
        role.setId_role(roleRepository.findByNamaRole("customer").getId_role());
        role.setNama_role("customer");
        users.setRole(role);

        try {
            return usersRepository.save(users);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("UK6dotkott2kjsp8vw4d0m25fb7")) {
                throw new CustomException("Email telah digunakan");
            }
            throw e; // lempar error lain jika bukan karena duplikat email
        }
    }

    public Optional<Users> cekEmailUsersCustomer(String email) {
        return usersRepository.findByEmail(email);
    }

    public String authenticateWithFirebase(String firebaseIdToken) {
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String email = decodedToken.getEmail();

            logger.info("Firebase token valid. Email: {}", email);


            Optional<Users> userOptional = usersRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User belum terdaftar di sistem");
            }

            Users user = userOptional.get();


            return jwtUtil.generateToken(user);

        } catch (FirebaseAuthException e) {
            logger.error("Firebase token tidak valid: {}", e.getMessage());
            throw new RuntimeException("Token Firebase tidak valid");
        }
    }
}
