package com.shwotime.userservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shwotime.userservice.common.ApiResponse;
import com.shwotime.userservice.type.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        Exception exception = (Exception) request.getAttribute("exception");
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        if(exception instanceof ExpiredJwtException){
            errorCode = ErrorCode.TOKEN_EXPIRED_EXCEPTION;
        }

        String responseBody = objectMapper.writeValueAsString(ApiResponse.error(request.getRequestURI(),errorCode));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);

    }
}
