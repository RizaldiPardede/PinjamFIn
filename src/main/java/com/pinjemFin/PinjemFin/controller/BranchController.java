package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;


    @GetMapping("/getAllBranch")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }


    @PostMapping("/getBranch")
    public ResponseEntity<?> getBranchById(@RequestBody Map<String, String> payload) {
        String idStr = payload.get("id_branch");

        if (idStr == null || idStr.isEmpty()) {
            return ResponseEntity.badRequest().body("id_branch tidak boleh kosong");
        }

        try {
            UUID id = UUID.fromString(idStr);
            return branchService.getBranchById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Format UUID tidak valid");
        }
    }


    @PostMapping("/addBranch")
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {
        Branch savedBranch = branchService.saveBranch(branch);
        return ResponseEntity.ok(savedBranch);
    }


    @PatchMapping("updateBranch/{id}")
    public ResponseEntity<Branch> updatePartialBranch(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates) {

        return branchService.updatePartialBranch(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
