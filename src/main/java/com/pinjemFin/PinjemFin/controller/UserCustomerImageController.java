package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.service.UserCustomerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/dokumen")
public class UserCustomerImageController {

    @Autowired
    private UserCustomerImageService userCustomerImageService;

    @PostMapping("/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("imageType") String imageType,
            @RequestParam("file") MultipartFile file) {

        String token = authHeader.substring(7); // remove "Bearer "

        try {
            String result = userCustomerImageService.uploadImage(token, imageType, file);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Gagal upload gambar: " + e.getMessage()));
        }
    }
}
