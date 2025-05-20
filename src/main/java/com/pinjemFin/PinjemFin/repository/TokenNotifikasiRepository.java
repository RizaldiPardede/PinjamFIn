package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.TokenNotifikasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenNotifikasiRepository extends JpaRepository<TokenNotifikasi, UUID> {
    Optional<TokenNotifikasi> findByToken(String token);

    @Query("SELECT t FROM TokenNotifikasi t WHERE t.usersCustomer.id_user_customer = :idCustomer")
    List<TokenNotifikasi> findTokensByCustomerId(@Param("idCustomer") UUID idCustomer);
}
