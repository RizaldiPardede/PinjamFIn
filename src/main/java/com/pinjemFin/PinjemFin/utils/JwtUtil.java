package com.pinjemFin.PinjemFin.utils;
import com.pinjemFin.PinjemFin.models.Users;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;




@Component
public class JwtUtil {

    private final Key SECRET_KEY;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 jam

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("SECRET_KEY (Raw): " + secret);
        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        System.out.println("SECRET_KEY (Base64 Encoded): " + SECRET_KEY);
    }

    private Key getSigningKey() {
        return SECRET_KEY;
    }

    // Generate Token
    public String generateToken(Users user) {
        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        System.out.println("Token Generated:");
        System.out.println("Issued At: " + issuedAt);
        System.out.println("Expires At: " + expiration);

        return Jwts.builder()
                .setSubject(user.getId_user().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Mengambil id dari token
    public String extractidUser(String token) {
        try {
            // Debugging token sebelum diproses
            System.out.println("Received Token: [" + token + "]");

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired at: " + e.getClaims().getExpiration());
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Malformed JWT: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    // Validasi token
    public boolean validateToken(String token, String email) {
        return email.equals(extractidUser(token)) && !isTokenExpired(token);
    }

    // Mengecek apakah token sudah kadaluarsa
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String extractEmailFromToken(String token) {
        // Menggunakan id_user untuk ekstrak email jika di set dalam subject
        return extractidUser(token); // Jika token subject berisi email
    }
}
