package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.PengajuanToPeminjamanRequest;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlafonService {
    @Autowired
    private PlafonRepository plafonRepository;

    public Plafon getplafonbycategory(String jenis_plafon) {
        return plafonRepository.findPlafonByJenis_plafon(jenis_plafon).get();
    }
    public List<Plafon> getAllPlafon() {
        return plafonRepository.findAllSorted();
    }

    public Plafon createPlafon(Plafon plafon) {
        return plafonRepository.save(plafon);
    }



}
