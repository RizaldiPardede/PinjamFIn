package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.JwtResponse;
import com.pinjemFin.PinjemFin.dto.LoginRequest;
import com.pinjemFin.PinjemFin.dto.LoginRequestEmployee;
import com.pinjemFin.PinjemFin.dto.RegisterRequest;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.service.AuthService;
import com.pinjemFin.PinjemFin.service.EmployeeService;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployeeService employeeService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/loginEmployee")
    public ResponseEntity<?> login(@RequestBody LoginRequestEmployee loginRequest) {
        Users users = employeeService.getUsersEmployee(loginRequest.getNip())
                .orElseThrow(() -> new RuntimeException("Invalid NIP or password")).getUsers();

        String token = authService.authenticateUser(users.getEmail(), loginRequest.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid NIP or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }



    @GetMapping("/getidUser")
    public ResponseEntity<?> testToken(@RequestHeader("Authorization") String token) {
        System.out.println("Received Token: [" + token + "]");

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        token = token.trim(); // Hapus spasi tambahan

        System.out.println("Processed Token: [" + token + "]");

        try {
            String id_user = jwtUtil.extractidUser(token);
            Map<String, String> response = new HashMap<>();
            response.put("id_user", id_user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token: " + e.getMessage());
        }
    }

    @PostMapping("/registerAkunCustomer")
    public ResponseEntity<?> registerakun(@RequestBody RegisterRequest RegisterRequest) {
        authService.registerUsers(RegisterRequest);

        String token = authService.authenticateUser(RegisterRequest.getUsername(), RegisterRequest.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }



}
