package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.PasswordResetToken;
import com.pinjemFin.PinjemFin.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    Optional<PasswordResetToken> findByUsers(Users users);
}
