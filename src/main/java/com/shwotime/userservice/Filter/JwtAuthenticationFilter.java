package com.shwotime.userservice.Filter;

import com.shwotime.userservice.exception.CustomRuntimeException;
import com.shwotime.userservice.type.ErrorCode;
import com.shwotime.userservice.util.JwtTokenProvider;
import com.shwotime.userservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = header.substring(7);
        if(jwtTokenProvider.validateToken(token)) {


            String userEmail = jwtUtil.getUserEmail();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        }else{
            System.out.println("Invalid Token");
        }

        filterChain.doFilter(request,response);




    }


}
