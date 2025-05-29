package com.pinjemFin.PinjemFin.security;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.RoleFeatureRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("permissionEvaluator")
public class FeaturePermissionEvaluator {

    private final UsersRepository usersRepository;
    private final RoleFeatureRepository roleFeatureRepository;

    @Autowired
    public FeaturePermissionEvaluator(UsersRepository userRepository, RoleFeatureRepository roleFeatureRepository) {
        this.usersRepository = userRepository;
        this.roleFeatureRepository = roleFeatureRepository;
    }

    public boolean hasAccess(Authentication authentication, String featureName) {
//        String userId = authentication.getName(); // dapat dari JWT
//        Users user = usersRepository.findById(UUID.fromString(userId)).orElse(null);
//        if (user == null || user.getRole() == null) return false;
//
//        List<Feature> allowedFeatures = roleFeatureRepository.findFeaturesByRoleId(user.getRole().getId_role());
//
//        return allowedFeatures.stream()
//                .anyMatch(f -> f.getFeature_name().equalsIgnoreCase(featureName));
        String userId = authentication.getName();
        System.out.println("Authentication.getName(): " + userId);

        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format: " + userId);
            return false;
        }

        Users user = usersRepository.findById(uuid).orElse(null);
        if (user == null) {
            System.out.println("User not found for UUID: " + userId);
            return false;
        }
        if (user.getRole() == null) {
            System.out.println("User role is null for user: " + userId);
            return false;
        }

        List<Feature> allowedFeatures = roleFeatureRepository.findFeaturesByRoleId(user.getRole().getId_role());

        return allowedFeatures.stream()
                .anyMatch(f -> f.getFeature_name().equalsIgnoreCase(featureName));
    }
}