package com.pinjemFin.PinjemFin.service;


import com.pinjemFin.PinjemFin.dto.RoleFeatureRequest;
import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Role_Feature;
import com.pinjemFin.PinjemFin.repository.FeatureRepository;
import com.pinjemFin.PinjemFin.repository.RoleFeatureRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleFeatureService     {

    @Autowired
    private RoleFeatureRepository roleFeatureRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FeatureRepository featureRepository;


    public Role_Feature createRoleFeature(Role_Feature roleFeature) {
        return roleFeatureRepository.save(roleFeature);
    }


    @Transactional
    public void attachFeature(RoleFeatureRequest request) {
        UUID roleId = request.getId_role();
        List<UUID> featureIds = request.getFeatureIds(); // Fitur yang sekarang dipilih user

        // 1. Ambil fitur yang sudah ada di database
        List<Role_Feature> existingRoleFeatures = roleFeatureRepository.findByRoleId(roleId);

        Set<UUID> existingFeatureIds = existingRoleFeatures.stream()
                .map(rf -> rf.getId_feature().getId_feature())
                .collect(Collectors.toSet());

        Set<UUID> requestedFeatureIds = new HashSet<>(featureIds);

        // 2. Hapus fitur yang sudah tidak dipilih
        for (Role_Feature rf : existingRoleFeatures) {
            if (!requestedFeatureIds.contains(rf.getId_feature().getId_feature())) {
                roleFeatureRepository.delete(rf);
            }
        }


        // 3. Tambah fitur baru yang belum ada di database
        for (UUID featureId : requestedFeatureIds) {
            if (!existingFeatureIds.contains(featureId)) {
                Role_Feature newRoleFeature = new Role_Feature();

                Role role = new Role();
                role.setId_role(roleId);
                newRoleFeature.setId_role(role);

                Feature feature = new Feature();
                feature.setId_feature(featureId);
                newRoleFeature.setId_feature(feature);

                roleFeatureRepository.save(newRoleFeature);
            }
        }
    }

    public List<UUID> getFeatureIdsByRole(UUID roleId) {
        // Misalnya menggunakan query untuk mendapatkan Role_Feature

        return roleFeatureRepository.findFeatureIdsByRole(roleId);
    }

    public List<String> getFeatureNamesByRole(UUID roleId) {
        // Misalnya menggunakan query untuk mendapatkan Role_Feature

        return roleFeatureRepository.findFeatureNamesByRole(roleId);
    }



}
