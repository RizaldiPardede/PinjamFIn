package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.ResponseMessage;
import com.pinjemFin.PinjemFin.dto.RoleFeatureRequest;
import com.pinjemFin.PinjemFin.models.Role_Feature;
import com.pinjemFin.PinjemFin.service.RoleFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rolefeature")
public class RoleFeatureController {
    @Autowired
    private RoleFeatureService roleFeatureService;

    @PostMapping("/create")
    public Role_Feature createRoleFeature(@RequestBody Role_Feature roleFeature) {
        return roleFeatureService.createRoleFeature(roleFeature);
    }


    @PostMapping("/attach")
    public ResponseEntity<?> attachFeatureToRole(@RequestBody RoleFeatureRequest request) {
        try {
            roleFeatureService.attachFeature(request);

            // Membuat ResponseMessage dengan setter
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Fitur berhasil di-update ke role.");

            return ResponseEntity.ok(responseMessage);
        } catch (RuntimeException e) {
            // Membuat ResponseMessage dengan setter
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Gagal update fitur ke role.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }

    @PostMapping("/getallrolefetures")
    public ResponseEntity<List<UUID>> getFeaturesByRole(@RequestBody RoleFeatureRequest request) {
        List<UUID> featureIds = roleFeatureService.getFeatureIdsByRole(request.getId_role());
        return ResponseEntity.ok(featureIds);
    }



}
