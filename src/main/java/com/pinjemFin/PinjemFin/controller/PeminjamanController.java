package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.PengajuanToPeminjamanRequest;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.PinjamanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/peminjaman")
public class PeminjamanController {

    @Autowired
    private PinjamanService pinjamanService;



    @PostMapping("/AddPeminjaman")
    public ResponseEntity<Pinjaman> createCustomer(@RequestBody PengajuanToPeminjamanRequest pengajuan) {
        Pinjaman savedCustomer = pinjamanService.addPeminjamamn(pengajuan);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PatchMapping("/updatePeminjaman/{id}")
    public ResponseEntity<Pinjaman> updatePartialPinjaman(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates) {
        Pinjaman updatedPinjaman = pinjamanService.partialUpdatePinjaman(id, updates);
        return ResponseEntity.ok(updatedPinjaman);
    }

    @GetMapping("/jmlhSlrhPinjamanLunas")
    public ResponseEntity<Map<String, Double>> getTotalPeminjamanLunasByUser(@RequestHeader("Authorization") String authHeader) {

        Double totalPinjamanLunas = pinjamanService.getTotalPeminjamanLunasByUser(authHeader);
        Map<String, Double> response = new HashMap<>();
        response.put("ttl_hutang_lunas", totalPinjamanLunas);
        return ResponseEntity.ok(response);
    }



}
