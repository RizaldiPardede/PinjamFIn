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


    public boolean hasAccess(String featureName) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }

            String token = authHeader.replace("Bearer ", "");
            String userId = jwtUtil.extractidUser(token); // ✅ Gunakan method buatanmu

            UUID uuid = UUID.fromString(userId);
            Users user = usersRepository.findById(uuid).orElse(null);
            if (user == null || user.getRole() == null) return false;

            List<Feature> allowedFeatures = roleFeatureRepository.findFeaturesByRoleId(user.getRole().getId_role());

            return allowedFeatures.stream()
                    .anyMatch(f -> f.getFeature_name().equalsIgnoreCase(featureName));

        } catch (Exception e) {
            System.out.println("Permission check failed: " + e.getMessage());
            return false;
        }
    }

}