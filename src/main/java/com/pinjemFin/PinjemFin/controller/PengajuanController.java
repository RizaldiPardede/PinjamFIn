package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.*;
import com.pinjemFin.PinjemFin.models.Pengajuan;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.NotificationService;
import com.pinjemFin.PinjemFin.service.PengajuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pengajuan")
public class PengajuanController {
    @Autowired
    PengajuanService pengajuanService;

    @Autowired
    CustomerService customerService;
    @Autowired
    NotificationService firebaseService;

    @PostMapping("/CreatePengajuan")
    public ResponseEntity<?> createPengajuan(@RequestBody PengajuanCustomerRequest pengajuanCustomerRequest,
                                                                  @RequestHeader("Authorization") String authHeader) {
        Double sisa_plafon = customerService.getUserCustomer(
                customerService.getUserCustomerIdFromToken(authHeader.substring(7)))
                .getSisa_plafon();
        if(pengajuanCustomerRequest.getAmount()<sisa_plafon){
            Pengajuan savedPengajuan = pengajuanService.createPengajuan(pengajuanCustomerRequest,authHeader);
            PengajuanResponse pengajuanResponse = new PengajuanResponse();
            pengajuanResponse.setAmount(savedPengajuan.getAmount());
            pengajuanResponse.setTenor(savedPengajuan.getTenor());
            pengajuanResponse.setBunga(savedPengajuan.getBunga());
            pengajuanResponse.setAngsuran(savedPengajuan.getAngsuran());
            pengajuanResponse.setTotal_payment(savedPengajuan.getTotal_payment());
            return new ResponseEntity<>(pengajuanResponse, HttpStatus.CREATED);
        }
        else {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("sisa plafon tidak cukup untuk melakukan pengajuan");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(responseMessage);
        }

    }

    @PostMapping("/getPengajuan")
    public Pengajuan getPengajuanById(@RequestBody PengajuanRequest pengajuanRequest) {
//        UUID idpengajuan = UUID.fromString(pengajuanRequest.get);
        return pengajuanService.getPengajuanById(pengajuanRequest.getId_pengajuan());
    }


    @PostMapping("/sendNotification")
    public ResponseEntity<ResponseMessage> sendNotification(@RequestBody NotificationRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Notification sent");
        firebaseService.sendNotification(request.getToken(), request.getTitle(), request.getBody());
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/getAllPengajuan")
    public List<Pengajuan> getAllPengajuan(@RequestHeader("Authorization")  String authHeader) {
        String token = authHeader.substring(7);
        UUID id_customer = customerService.getUserCustomerIdFromToken(token);
        return pengajuanService.getAllPengajuanByCustomerId(id_customer);
    }



}
