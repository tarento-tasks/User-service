package com.example.Userservice.config;

import com.example.Userservice.Model.InvalidatedToken;
import com.example.Userservice.Model.User;
import com.example.Userservice.Repository.InvalidatedTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private Key secretKeyDecoded;
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtUtil(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT Secret Key must be at least 32 bytes long");
        }
        this.secretKeyDecoded = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Generate JWT token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("authorities", List.of("ROLE_" + user.getRole().getRoleName()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKeyDecoded, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract username
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKeyDecoded)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Extract all claims
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKeyDecoded)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Validate token
    public Boolean validateToken(String token, String email) {
        final String extractedEmail = extractUsername(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token) && !isTokenInvalid(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // ✅ 🚨 Store invalidated token in database
    public void invalidateToken(String token) {
        System.out.println("Invalidating token: " + token);
        Date expirationDate = extractAllClaims(token).getExpiration();
        InvalidatedToken invalidatedToken = new InvalidatedToken(token, expirationDate);
        invalidatedTokenRepository.save(invalidatedToken);
    }

    // ✅ 🚨 Check if token is invalidated
    public boolean isTokenInvalid(String token) {
        return invalidatedTokenRepository.findByToken(token).isPresent();
    }
}
