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



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }



    @GetMapping("/test")
    public ResponseEntity<?> testToken(@RequestHeader("Authorization") String token) {
        System.out.println("Received Token: [" + token + "]");

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        token = token.trim(); // Hapus spasi tambahan

        System.out.println("Processed Token: [" + token + "]");

        try {
            String email = jwtUtil.extractEmail(token);
            return ResponseEntity.ok("Email: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token: " + e.getMessage());
        }
    }


//    public String testToken(@RequestHeader("Authorization") String token) {
//        // Hapus prefix "Bearer " sebelum diproses
//        if (token.startsWith("bearer ")||token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        String email = jwtUtil.extractEmail(token);
//        return "Email: " + email;
//    }
}
