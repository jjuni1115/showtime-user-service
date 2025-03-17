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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/user/login") || path.equals("/user/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            if (token !=null && jwtTokenProvider.validateToken(token)) {


                String userEmail = jwtUtil.getUserEmail();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            }
        } catch (Exception e){
            request.setAttribute("exception",e);
        }

        filterChain.doFilter(request, response);


    }


}
