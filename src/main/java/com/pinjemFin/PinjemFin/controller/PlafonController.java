package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.service.PlafonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/plafon")
public class PlafonController {


    @Autowired
    PlafonService plafonService;

    @GetMapping("/getAllPlafon")
    public List<Plafon> getAllPlafon() {
        return plafonService.getAllPlafon(); // Menggunakan method custom yang sudah kamu buat
    }

    @PostMapping("/createPlafon")
    public Plafon createPlafon(@RequestBody Plafon plafon) {
        return plafonService.createPlafon(plafon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlafon(@PathVariable UUID id, @RequestBody Plafon plafon) {
        Plafon updated = plafonService.updatePlafon(id, plafon);
        return ResponseEntity.ok().body(Map.of(
                "message", "Plafon berhasil diperbarui",
                "data", updated
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlafon(@PathVariable UUID id) {
        Plafon deleted = plafonService.deletePlafon(id);
        return ResponseEntity.ok().body(Map.of(
                "message", "Plafon berhasil dihapus",
                "data", deleted
        ));
    }
}
