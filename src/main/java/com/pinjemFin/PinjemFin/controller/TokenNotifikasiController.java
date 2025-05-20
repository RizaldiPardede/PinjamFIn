package com.pinjemFin.PinjemFin.controller;


import com.pinjemFin.PinjemFin.dto.ResponseMessage;
import com.pinjemFin.PinjemFin.dto.TokenNotifikasiRequest;
import com.pinjemFin.PinjemFin.models.TokenNotifikasi;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.service.CustomerService;
import com.pinjemFin.PinjemFin.service.TokenNotifikasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tokenNotifikasi")
public class TokenNotifikasiController {
    @Autowired
    private TokenNotifikasiService tokenNotifikasiService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/addTokenNotifikasi")
    public ResponseEntity<ResponseMessage> addToken(@RequestHeader("Authorization")String autheader
            , @RequestBody TokenNotifikasiRequest tokenNotifikasiRequest) {
        ResponseMessage responseMessage = new ResponseMessage();
        UUID idusersCustomer = customerService.getUserCustomerIdFromToken(autheader.substring(7));
        TokenNotifikasi tokenNotifikasi = tokenNotifikasiService.addToken(tokenNotifikasiRequest.getTokenNotifikasi(),idusersCustomer);

        if (tokenNotifikasi != null) {
            responseMessage.setMessage("Token notifikasi added");
        }

        else {
            responseMessage.setMessage("Token not found");
        }
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/clearUserCustomerNotifikasi")
    public ResponseEntity<ResponseMessage> clearCustomerFromToken(@RequestHeader("Authorization")String autheader
            ,@RequestBody TokenNotifikasiRequest tokenNotifikasiRequest) {
        ResponseMessage responseMessage = new ResponseMessage();
        TokenNotifikasi tokenNotifikasi = tokenNotifikasiService.clearUserCustomerByToken(tokenNotifikasiRequest.getTokenNotifikasi());

        if (tokenNotifikasi != null) {
            responseMessage.setMessage("Token notifikasi added");
        }

        else {
            responseMessage.setMessage("Token not found");
        }
        return ResponseEntity.ok(responseMessage);
    }
}
