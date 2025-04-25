package com.shwotime.userservice.util;

import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;

    private final SecretKey key;
    private final long expirationTime;

    public JwtTokenProvider(UserRepository userRepository, @Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.expiration}") long expirationTime) {
        this.userRepository = userRepository;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    public String generateToken(String userEmail) {

        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("userEmail", user.getEmail())
                .claim("userName", user.getName())
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
    }


    public String generateRefreshToken(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("userEmail", user.getEmail())
                .claim("userName", user.getName())
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
    }


    public Boolean validateToken(String token) {

            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;


    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(key)
                .build().parseSignedClaims(token).getPayload();
    }


}
