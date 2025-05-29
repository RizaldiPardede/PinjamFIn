package com.pinjemFin.PinjemFin.security;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.repository.RoleFeatureRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
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
    @Value("${jwt.secret}")

    private String secretKey;
    @Autowired
    public FeaturePermissionEvaluator(UsersRepository userRepository, RoleFeatureRepository roleFeatureRepository) {
        this.usersRepository = userRepository;
        this.roleFeatureRepository = roleFeatureRepository;
    }

    public boolean hasAccess(Authentication authentication, String featureName) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String userId = getSubjectFromToken();
        if (userId == null) {
            System.out.println("Token subject (sub) not found");
            return false;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format: " + userId);
            return false;
        }

        Users user = usersRepository.findById(uuid).orElse(null);
        if (user == null || user.getRole() == null) {
            System.out.println("User or Role not found for UUID: " + userId);
            return false;
        }

        List<Feature> allowedFeatures = roleFeatureRepository.findFeaturesByRoleId(user.getRole().getId_role());

        return allowedFeatures.stream()
                .anyMatch(f -> f.getFeature_name().equalsIgnoreCase(featureName));
    }

    private String getSubjectFromToken() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr == null) return null;

            HttpServletRequest request = attr.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

            String token = authHeader.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes()) // gunakan .getBytes() untuk versi 0.9.1
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // ini adalah UUID user dari JWT "sub"
        } catch (Exception e) {
            System.out.println("Error parsing JWT: " + e.getMessage());
            return null;
        }
    }
}