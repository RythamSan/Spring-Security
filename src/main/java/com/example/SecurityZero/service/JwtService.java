package com.example.SecurityZero.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    public JwtService(@Value("${jwt.secret}") String secret){
        this.key= Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)  //byte[]
        );
    }
    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Claims claims=extractAllClaims(token);
            Date expiration=claims.getExpiration();

            return expiration!=null && expiration.after(new Date());
        }catch (JwtException | IllegalArgumentException ex){
            return false;
        }

    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
