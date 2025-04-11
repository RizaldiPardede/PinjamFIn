package com.pinjemFin.PinjemFin.repository;


import com.pinjemFin.PinjemFin.models.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Pengajuan p SET p.status = :status WHERE p.id_pengajuan = :id")
    void updateStatusById(UUID id, String status);

}
