package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.service.PlafonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
