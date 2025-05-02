package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAll();


    @Query("SELECT r FROM Role r WHERE r.nama_role = :namaRole")
    Role findByNamaRole(@Param("namaRole") String namaRole);


}
