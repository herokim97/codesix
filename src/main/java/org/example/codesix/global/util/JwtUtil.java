package org.example.codesix.global.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("30000000")
    private long expiryMillis;

    private final UserRepository userRepository;

    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported", e.getMessage());
        }
        return false;
    }

    public String getUserName(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public String generateToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();
        return this.generateToken(username);
    }

    private String generateToken(String email) throws EntityNotFoundException {
        User user = this.userRepository.findByEmailOrElseThrow(email);

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }


}
