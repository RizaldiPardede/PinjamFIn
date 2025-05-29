package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.GetProfileResponse;
import com.pinjemFin.PinjemFin.service.UserCustomerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/dokumen")
public class UserCustomerImageController {

    @Autowired
    private UserCustomerImageService userCustomerImageService;

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_uploadImage')")
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

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getProfileImage')")
    @PostMapping("/getProfileImage")
    public ResponseEntity<GetProfileResponse> getProfileImage(@RequestHeader("Authorization") String authHeader) throws Exception {
        String token = authHeader.substring(7); // remove "Bearer "
        GetProfileResponse getProfileResponse = new GetProfileResponse();
        getProfileResponse.setImageProfile(userCustomerImageService.getImageProfile(token));
        return ResponseEntity.ok(getProfileResponse);
    }
}
