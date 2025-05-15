package com.pinjemFin.PinjemFin.repository;
;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PinjamanRepository extends JpaRepository<Pinjaman, UUID> {

    @Query("SELECT COALESCE(SUM(p.total_payment), 0) FROM Pinjaman p WHERE p.sisa_tenor = 0 AND p.id_user_customer.id_user_customer = :userId")
    Double getTotalPeminjamanLunasByUser(UUID userId);

    @Query("SELECT COALESCE(SUM(p.total_payment), 0) FROM Pinjaman p WHERE p.id_user_customer.id_user_customer = :userId")
    Double getTotalPeminjamanByUser(UUID userId);
}
