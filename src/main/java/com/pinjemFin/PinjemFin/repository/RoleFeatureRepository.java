package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Role_Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleFeatureRepository extends JpaRepository<Role_Feature, UUID> {
    @Query("SELECT rf.id_feature FROM Role_Feature rf WHERE rf.id_role.id_role = :idRole")
    List<Feature> findFeaturesByRoleId(@Param("idRole") UUID idRole);

//    Optional<Role_Feature> findById_roleAndId_feature(Role role, Feature feature);

    @Query("SELECT rf FROM Role_Feature rf WHERE rf.id_role = :role AND rf.id_feature = :feature")
    Optional<Role_Feature> findByRoleAndFeature(@Param("role") Role role, @Param("feature") Feature feature);

    @Query("SELECT rf FROM Role_Feature rf WHERE rf.id_role.id_role = :roleId")
    List<Role_Feature> findByRoleId(@Param("roleId") UUID roleId);

    @Query("SELECT rf.id_feature.id_feature FROM Role_Feature rf WHERE rf.id_role.id_role = :roleId")
    List<UUID> findFeatureIdsByRole(UUID roleId);

    @Query("SELECT rf.id_feature.feature_name FROM Role_Feature rf WHERE rf.id_role.id_role = :roleId")
    List<String> findFeatureNamesByRole(UUID roleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Role_Feature rf WHERE rf.id_role.id_role = :idRole")
    void deleteByIdRole(@Param("idRole") UUID idRole);
}
