package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.PengajuanToPeminjamanRequest;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.meta.When;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Plafon deletePlafon(UUID idplafon) {
        Optional<Plafon> plafon = plafonRepository.findById(idplafon);
        if (plafon.isPresent()) {
            Plafon plafonopt = plafon.get();

            switch (plafonopt.getJenis_plafon().toLowerCase()) {
                case "bronze":
                case "silver":
                case "gold":
                case "platinum":
                    throw new IllegalArgumentException("Plafon default '" + plafonopt.getJenis_plafon() + "' tidak dapat dihapus");
                default:
                    plafonRepository.deleteById(idplafon);
                    return plafonopt;
            }
        } else {
            throw new EntityNotFoundException("Plafon tidak ditemukan");
        }
    }

    public Plafon updatePlafon(UUID id, Plafon updatedPlafon) {
        Plafon existingPlafon = plafonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plafon tidak ditemukan dengan id: " + id));

        existingPlafon.setJenis_plafon(updatedPlafon.getJenis_plafon());
        existingPlafon.setJumlah_plafon(updatedPlafon.getJumlah_plafon());
        existingPlafon.setBunga(updatedPlafon.getBunga());

        // tambahkan field lain jika ada

        return plafonRepository.save(existingPlafon);
    }



}
