package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.CekUpdateAkunRequest;
import com.pinjemFin.PinjemFin.dto.DetailCustomerRequest;
import com.pinjemFin.PinjemFin.dto.PengajuanCustomerRequest;
import com.pinjemFin.PinjemFin.dto.SimulasiRequest;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.PasswordResetService;
import com.pinjemFin.PinjemFin.service.PengajuanService;
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
    private PengajuanService pengajuanService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/CekUpdateAkun")
    public ResponseEntity<?> cekUpdateAkun(@RequestBody CekUpdateAkunRequest request) {
        UUID id_user = request.getId_user(); // Ambil ID dari request body
        return CustomerService.cekUpdateAkun(id_user); // Panggil service untuk mendapatkan response
    }

    @PostMapping("/AddDetailAkun")
    public ResponseEntity<UsersCustomer> createCustomer(@RequestBody DetailCustomerRequest detailCustomerRequest
            ,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "
        UsersCustomer savedCustomer = CustomerService.addCustomer(detailCustomerRequest,token);
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

//    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getplafon')")
    @GetMapping("/getPlafon")
    public ResponseEntity<UsersCustomer> getplafon(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        // Hapus "Bearer ";
        return ResponseEntity.ok(CustomerService.getPlafon(token));
    }

    @PostMapping("/getSimulasi")
    public ResponseEntity<PengajuanCustomerRequest> getSimulasi(@RequestBody SimulasiRequest simulasiRequest, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(pengajuanService.getSimulasiPengajuan(simulasiRequest.getAmount(),simulasiRequest.getTenor(),token));
    }




}
