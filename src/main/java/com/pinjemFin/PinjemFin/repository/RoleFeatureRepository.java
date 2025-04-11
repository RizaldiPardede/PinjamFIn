package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role_Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleFeatureRepository extends JpaRepository<Role_Feature, UUID> {
    @Query("SELECT rf.id_feature FROM Role_Feature rf WHERE rf.id_role.id_role = :idRole")
    List<Feature> findFeaturesByRoleId(@Param("idRole") UUID idRole);
}
