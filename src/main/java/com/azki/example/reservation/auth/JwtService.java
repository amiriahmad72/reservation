
package com.azki.example.reservation.auth;

import com.azki.example.reservation.user.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final SecurityProperties securityProperties;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(securityProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(CustomUserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + securityProperties.accessExp()))
                .signWith(key())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
