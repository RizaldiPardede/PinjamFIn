package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.service.PlafonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
