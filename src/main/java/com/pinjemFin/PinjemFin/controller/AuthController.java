package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.*;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.service.*;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private RoleFeatureService roleFeatureService;

    @Autowired
    private AccountActivationService accountActivationService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        ResponseMessage responseMessage = new ResponseMessage();

            if (token == null || token.isEmpty()) {
                responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }

            if (token.contains("Password tidak cocok")) {
                responseMessage.setMessage(token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }

            if (token.contains("Email belum di verifikasi")) {
                responseMessage.setMessage(token);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMessage);
            }

            if (token.contains("User dengan email")) {
                responseMessage.setMessage(token);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }




        return ResponseEntity.ok(new JwtResponse(token));
    }



    @PostMapping("/loginWithgoogle")
    public ResponseEntity<?> loginWithgoogle(@RequestBody loginGoogleRequest loginRequest) {
        String token = authService.authenticateWithFirebase(loginRequest.getFirebaseIdToken());
        ResponseMessage responseMessage = new ResponseMessage();

        if (token == null || token.isEmpty()) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
        }

        if (token.contains("Password tidak cocok")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
        }

        if (token.contains("Email belum di verifikasi")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMessage);
        }

        if (token.contains("User dengan email")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }



        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/loginEmployee")
    public ResponseEntity<?> login(@RequestBody LoginRequestEmployee loginRequest) {
        Users users = employeeService.getUsersEmployee(loginRequest.getNip())
                .orElseThrow(() -> new RuntimeException("Invalid NIP or password")).getUsers();

        String token = authService.authenticateUser(users.getEmail(), loginRequest.getPassword());
        ResponseMessage responseMessage = new ResponseMessage();

        if (token == null || token.isEmpty()) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
        }

        if (token.contains("Password tidak cocok")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
        }

        if (token.contains("Email belum di verifikasi")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMessage);
        }

        if (token.contains("User dengan email")) {
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }


        // Ambil fitur dari role yang dimiliki user
        List<String> features = roleFeatureService.getFeatureNamesByRole(users.getRole().getId_role());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setFeatures(features);
        loginResponse.setIsActive(users.getIsActive());
        return ResponseEntity.ok(loginResponse);
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

    @PostMapping("/registerAuthGoogle")
    public ResponseEntity<?> registerAuthGoogle(@RequestBody RegisterRequest RegisterRequest) {
        authService.registerUsersAuthGoogle(RegisterRequest);

        String token = authService.authenticateUser(RegisterRequest.getUsername(), RegisterRequest.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        return passwordResetService.handleForgotPassword(request.get("email"));
    }


    @GetMapping("/reset-password")
    public ResponseEntity<Map<String, String>> showResetForm(@RequestParam String token) {
        return passwordResetService.handleShowResetForm(token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody Map<String, String> request
    ) {
        return passwordResetService.handleResetPassword(request.get("token"), request.get("new_password"));
    }

    @GetMapping("/test")
    public ResponseEntity<String> showResetForm() {
        return ResponseEntity.status(HttpStatus.OK).body("test");
    }

    @PostMapping("/emailActivation")
    public ResponseEntity<ResponseMessage> emailActivation(@RequestBody TokenActivationRequest tokenActivationRequest ) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(accountActivationService.ActivationEmail(tokenActivationRequest.getTokenActivation()));
        return ResponseEntity.ok(responseMessage);
    }
}
