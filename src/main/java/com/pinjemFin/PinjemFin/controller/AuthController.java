package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.JwtResponse;
import com.pinjemFin.PinjemFin.dto.LoginRequest;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private com.pinjemFin.PinjemFin.services.AuthService authService;

//    // Login API
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        return authService.authenticateUser(username, password);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/test")
    public String testToken(@RequestHeader("Authorization") String token) {
        // Hapus prefix "Bearer " sebelum diproses
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtUtil.extractEmail(token);
        return "Email: " + email;
    }
}
