package com.shwotime.userservice.handler;

import com.shwotime.userservice.type.Role;
import com.shwotime.userservice.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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


        if(role!=null && role.equals(Role.GUSET.getRoles())){

            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/signup")
                    .queryParam("email",email)
                    .queryParam("name",name)
                    .build().encode().toUriString();

            response.sendRedirect(redirectUrl);

        }else{

            String token = jwtTokenProvider.generateToken(email);



            String refreshToken = jwtTokenProvider.generateRefreshToken(email);

            Cookie cookie = new Cookie("refreshToken",refreshToken);
            cookie.setSecure(true);
            cookie.setPath("/user/reissue");



            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth").queryParam("token",token).build().toUriString();

            response.addCookie(cookie);

            response.sendRedirect(redirectUrl);



        }


    }
}
