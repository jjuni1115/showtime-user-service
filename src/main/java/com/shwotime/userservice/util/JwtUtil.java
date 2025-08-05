package com.shwotime.userservice.util;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public String getUserEmail(){

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);

        return jwtTokenProvider.extractAllClaims(token).get("userEmail").toString();
    }

    public String getUserName(){

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);

        return jwtTokenProvider.extractAllClaims(token).get("userName").toString();
    }

    public String getUserNickName(){

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);

        return jwtTokenProvider.extractAllClaims(token).get("nickName").toString();
    }

    public Long getUserId(){

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);

        return Long.parseLong(jwtTokenProvider.extractAllClaims(token).get("userId").toString());
    }



    public String getUserEmail(String token){
        return jwtTokenProvider.extractAllClaims(token).get("userEmail").toString();
    }




}
