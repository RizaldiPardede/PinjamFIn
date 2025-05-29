package com.pinjemFin.PinjemFin.security;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.RoleFeatureRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.UUID;

@Component("permissionEvaluator")
public class FeaturePermissionEvaluator {

    private final UsersRepository usersRepository;
    private final RoleFeatureRepository roleFeatureRepository;
    private final JwtUtil jwtUtil;



    @Autowired
    public FeaturePermissionEvaluator(UsersRepository userRepository, RoleFeatureRepository roleFeatureRepository,JwtUtil jwtUtil) {
        this.usersRepository = userRepository;
        this.roleFeatureRepository = roleFeatureRepository;
        this.jwtUtil = jwtUtil;
    }


    public boolean hasAccess(Authentication authentication, String featureName) {
        try {
            System.out.println("=== Start hasAccess ===");

            if (authentication == null) {
                System.out.println("Authentication is null");
            } else {
                System.out.println("Authentication name: " + authentication.getName());
                System.out.println("Authentication isAuthenticated: " + authentication.isAuthenticated());
                System.out.println("Authentication credentials: " + authentication.getCredentials());
            }

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            System.out.println("Authorization header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Invalid or missing Authorization header");
                return false;
            }

            String token = authHeader.replace("Bearer ", "");
            System.out.println("Extracted token: " + token);

            String userId = jwtUtil.extractidUser(token);
            System.out.println("Extracted userId from token: " + userId);

            UUID uuid;
            try {
                uuid = UUID.fromString(userId);
                System.out.println("Parsed UUID: " + uuid);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format: " + userId);
                return false;
            }

            Users user = usersRepository.findById(uuid).orElse(null);
            if (user == null) {
                System.out.println("User not found in database for UUID: " + uuid);
                return false;
            }
            System.out.println("User found: " + user.getNama());
            if (user.getRole() == null) {
                System.out.println("User role is null");
                return false;
            }
            System.out.println("User role: " + user.getRole().getNama_role());

            List<Feature> allowedFeatures = roleFeatureRepository.findFeaturesByRoleId(user.getRole().getId_role());
            System.out.println("Allowed features for role: " + allowedFeatures.stream()
                    .map(Feature::getFeature_name)
                    .toList());

            boolean hasFeature = allowedFeatures.stream()
                    .anyMatch(f -> f.getFeature_name().equalsIgnoreCase(featureName));
            System.out.println("Has access to feature '" + featureName + "': " + hasFeature);

            System.out.println("=== End hasAccess ===");
            return hasFeature;

        } catch (Exception e) {
            System.out.println("Permission check failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}