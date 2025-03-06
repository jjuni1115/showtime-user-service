package com.shwotime.userservice.handler;

import com.shwotime.userservice.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class CustomOAuthSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");


        if(role!=null && role.equals("ROLE_GUEST")){

            //TODO: redirect to registration page
            response.sendRedirect("http://localhost:5173/signup");

        }else{

            //TODO: generate token and redirect to home page
            String token = jwtTokenProvider.generateToken(email);

            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/home").queryParam("token",token).build().toUriString();

            response.sendRedirect(redirectUrl);



        }


    }
}
