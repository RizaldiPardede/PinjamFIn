package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.PengajuanCustomerRequest;
import com.pinjemFin.PinjemFin.dto.PengajuanRequest;
import com.pinjemFin.PinjemFin.dto.PengajuanToPeminjamanRequest;
import com.pinjemFin.PinjemFin.models.Pengajuan;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.repository.PengajuanRepository;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.PengajuanEmployeeService;
import com.pinjemFin.PinjemFin.service.PengajuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pengajuan")
public class PengajuanController {
    @Autowired
    PengajuanService pengajuanService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/CreatePengajuan")
    public ResponseEntity<pengajuan_userEmployee> createPengajuan(@RequestBody PengajuanCustomerRequest pengajuanCustomerRequest,
                                                                  @RequestHeader("Authorization") String authHeader) {
        Pengajuan pengajuan = new Pengajuan();
        pengajuan.setAmount(pengajuanCustomerRequest.getAmount());
        pengajuan.setTenor(pengajuanCustomerRequest.getTenor());
        pengajuan.setStatus("bckt_marketing");
        pengajuan.setAngsuran(pengajuanCustomerRequest.getAngsuran());
        pengajuan.setBunga(pengajuanCustomerRequest.getBunga());
        pengajuan.setTotal_payment(pengajuanCustomerRequest.getTotal_payment());
        String token = authHeader.substring(7); // Hapus "Bearer "
        pengajuan.setId_user_customer(customerService.getUserCustomer(customerService.getUserCustomerIdFromToken(token)));

        pengajuan_userEmployee savedPengajuan = pengajuanService.createPengajuan(pengajuan);

        return new ResponseEntity<>(savedPengajuan, HttpStatus.CREATED);
    }

    @PostMapping("/getPengajuan")
    public Pengajuan getPengajuanById(@RequestBody PengajuanRequest pengajuanRequest) {
//        UUID idpengajuan = UUID.fromString(pengajuanRequest.get);
        return pengajuanService.getPengajuanById(pengajuanRequest.getId_pengajuan());
    }

}
