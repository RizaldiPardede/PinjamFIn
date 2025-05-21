package com.pinjemFin.PinjemFin.repository;


import com.pinjemFin.PinjemFin.models.Pengajuan;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Pengajuan p SET p.status = :status WHERE p.id_pengajuan = :id")
    void updateStatusById(UUID id, String status);

    @Query("SELECT p FROM Pengajuan p WHERE p.id_user_customer.id_user_customer = :idCustomer")
    List<Pengajuan> findAllByCustomerId(@Param("idCustomer") UUID idCustomer);


    @Query("SELECT COUNT(p) > 0 FROM Pengajuan p WHERE p.id_user_customer = :user AND p.status IN :statuses")
    boolean existsByUserAndStatusIn(@Param("user") UsersCustomer user, @Param("statuses") List<String> statuses);
}
