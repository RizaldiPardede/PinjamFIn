package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.ActivationToken;

import com.pinjemFin.PinjemFin.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    ActivationToken findBytokenActivation(String token);

    Optional<ActivationToken> findByUsers(Users users);
}
