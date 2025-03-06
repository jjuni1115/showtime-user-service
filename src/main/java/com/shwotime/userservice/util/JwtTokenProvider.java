package com.shwotime.userservice.util;

import com.shwotime.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;

    private final Key key;
    private final long expirationTime;

    public JwtTokenProvider(UserRepository userRepository, @Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.expiration}") long expirationTime) {
        this.userRepository = userRepository;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationTime = expirationTime;
    }

    public String generateToken(String userEmail){
        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    //TODO: generate refresh token and validate token methods





}
