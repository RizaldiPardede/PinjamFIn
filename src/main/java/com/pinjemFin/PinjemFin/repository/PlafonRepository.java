package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Plafon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlafonRepository extends JpaRepository<Plafon, UUID> {
    @Query("SELECT p FROM Plafon p ORDER BY p.jumlah_plafon ASC")
    List<Plafon> findAllSorted();
}
