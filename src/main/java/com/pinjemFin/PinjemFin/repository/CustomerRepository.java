package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository <UsersCustomer,UUID>{
//    Optional<UsersCustomer> findByusers_id_user(UUID idUser);


    @Transactional(readOnly = true)
    @Query("SELECT uc FROM UsersCustomer uc WHERE uc.users.id_user = :idUser")
    Optional<UsersCustomer> findByUsersIdUser(@Param("idUser") UUID idUser);
}
