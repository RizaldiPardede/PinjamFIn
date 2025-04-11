package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.CekUpdateAkunRequest;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService CustomerService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/CekUpdateAkun")
    public ResponseEntity<?> cekUpdateAkun(@RequestBody CekUpdateAkunRequest request) {
        UUID id_user = request.getId_user(); // Ambil ID dari request body
        return CustomerService.cekUpdateAkun(id_user); // Panggil service untuk mendapatkan response
    }

    @PostMapping("/AddDetailAkun")
    public ResponseEntity<UsersCustomer> createCustomer(@RequestBody UsersCustomer usersCustomer) {
        UsersCustomer savedCustomer = CustomerService.addCustomer(usersCustomer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }


    @PatchMapping("UpdateDetailCustomer/{id}")
    public ResponseEntity<UsersCustomer> updatePartialCustomer(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates) {
        UsersCustomer updatedCustomer = CustomerService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/getIdUserCustomer")
    public
    ResponseEntity<Map<String, UUID>>getMyUserCustomerId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "
        UUID idUserCustomer = CustomerService.getUserCustomerIdFromToken(token);

        Map<String, UUID> response = new HashMap<>();
        response.put("id_user_customer", idUserCustomer);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getplafon')")
    @GetMapping("/getPlafon")
    public ResponseEntity<UsersCustomer> getplafon(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        // Hapus "Bearer ";
        return ResponseEntity.ok(customerService.getPlafon(token));
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


}
