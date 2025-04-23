package sia.plants.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import sia.plants.model.user.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    private final Key keyToken = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getUserId().toString());
        claims.put("admin", user.isAdmin());

        UUID orgId = (user.getOrganization() != null) ? user.getOrganization().getOrganizationId() : null;
        claims.put("organizationId", orgId != null ? orgId.toString() : null);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 день
                .signWith(keyToken)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyToken).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUserId(String token) {
        return extractClaim(token, "userId", String.class);
    }

    public String extractOrganizationId(String token) {
        return extractClaim(token, "organizationId", String.class);
    }

    public Boolean extractIsAdmin(String token) {
        return extractClaim(token, "admin", Boolean.class);
    }
    public <T> T extractClaim(String token, String key, Class<T> clazz) {
        return Jwts.parserBuilder()
                .setSigningKey(keyToken)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(key, clazz);
    }
}
