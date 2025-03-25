package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.CekUpdateAkunRequest;
import com.pinjemFin.PinjemFin.dto.LoginRequest;
import com.pinjemFin.PinjemFin.service.CekUpdateAkunService;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CekUpdateAkunService CekUpdateAkunService;

    @PostMapping("/CekUpdateAkun")
    public ResponseEntity<?> cekUpdateAkun(@RequestBody CekUpdateAkunRequest request) {
        UUID id_user = request.getId_user(); // Ambil ID dari request body
        return CekUpdateAkunService.cekUpdateAkun(id_user); // Panggil service untuk mendapatkan response
    }

}
