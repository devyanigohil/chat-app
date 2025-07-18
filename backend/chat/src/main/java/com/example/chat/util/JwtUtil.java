package com.example.chat.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

        SecretKey secretKey = Keys.hmacShaKeyFor("jUohSTXqJ6dreJClcrWcx3kMz71EocfL".getBytes(StandardCharsets.UTF_8));

        public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1)) // 10 hours
                .signWith(secretKey)
                .compact();
          }

        
        public String extractUsername(String token) {
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            Jws<Claims> jws = parser.parseSignedClaims(token);
            return jws.getPayload().getSubject();
        }

        public boolean validateToken(String token,String expectedUsername) {
             String username = extractUsername(token);
            return username.equals(expectedUsername) && !isTokenExpired(token);
        }

       private boolean isTokenExpired(String token) {
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Date expiration = parser.parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

        
}
