package com.learnzy.backend.utils;

import com.learnzy.backend.config.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Autowired
    private Config config;

    public String generateToken(Long userId){
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + config.getJwtExpiration()*1000))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    public Long extractUserId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public Long getExpirationTime(){
        return config.getJwtExpiration();
    }

    private String getSecretKey(){
        return Base64.getEncoder().encodeToString(config.getJwtSecret().getBytes());
    }
}
