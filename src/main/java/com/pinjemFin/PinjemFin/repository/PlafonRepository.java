package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Plafon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlafonRepository extends JpaRepository<Plafon, UUID> {
    @Query("SELECT p FROM Plafon p ORDER BY p.jumlah_plafon ASC")
    List<Plafon> findAllSorted();

    @Query("SELECT p FROM Plafon p WHERE p.jenis_plafon = :jenis_plafon")
    Optional<Plafon> findPlafonByJenis_plafon(@Param("jenis_plafon") String jenis_plafon);



}
