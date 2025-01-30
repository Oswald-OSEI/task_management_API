package taskmanagement.taskmanagementapi.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.common.exceptions.TokenValidationExceptionWrapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretString;

    private static final int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30 minutes
    private static final int REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 1; // 7 days

    private SecretKey getSigningKey(String secretString) {
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> userDetails, int expirationTime, String secret) {
        return Jwts.builder()
                .claims(userDetails)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(secret), Jwts.SIG.HS512) // Changed to HS512 to match the token
                .compact();
    }

    public String generateAccessToken(long id, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        return createToken(claims, ACCESS_TOKEN_EXPIRATION, secretString);
    }

    public String generateRefreshToken(long id, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        return createToken(claims, REFRESH_TOKEN_EXPIRATION, refreshSecretString);
    }

    public TokenValidationExceptionWrapper validateToken(String token, String secret) {
        try {
            // Remove "Bearer " prefix if present
            token = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            
            Jwts.parser()
                    .verifyWith(getSigningKey(secret))
                    .build()
                    .parseSignedClaims(token);
            return new TokenValidationExceptionWrapper(true, null);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            //log.error("Token validation failed: {}", e.getMessage());
            return new TokenValidationExceptionWrapper(false, e);
        }
    }

    private Claims extractClaims(String token, String secret) {
        // Remove "Bearer " prefix if present
        token = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        
        return Jwts.parser()
                .verifyWith(getSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long extractId(String token, String secret) {
        return extractClaims(token, secret).get("id", Long.class);
    }

    public String extractUsername(String token, String secret) {
        return extractClaims(token, secret).get("username", String.class);
    }

    public Date extractExpiration(String token, String secret) {
        return extractClaims(token, secret).getExpiration();
    }
}