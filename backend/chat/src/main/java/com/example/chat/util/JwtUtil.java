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


        public String generateAccessToken(String username) {
            return generateToken(username, 3L * 60 * 1000); // 20 min
        }

        public String generateRefreshToken(String username) {
            return generateToken(username, 3L* 60 * 60 * 1000); // 3 hours
        }


        public String generateToken(String username, Long expirationTime) {
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationTime))
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

       public boolean isTokenExpired(String token) {
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Date expiration = parser.parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

        
}
