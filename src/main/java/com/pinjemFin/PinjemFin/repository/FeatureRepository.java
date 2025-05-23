package com.pinjemFin.PinjemFin.repository;
import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    List<Feature> findAll();

    @Query("SELECT f FROM Feature f WHERE f.feature_name = :name")
    Feature findByFeatureName(@Param("name") String name);
}
