package com.shwotime.userservice.config;

import com.shwotime.userservice.Filter.JwtAuthenticationFilter;
import com.shwotime.userservice.exception.CustomAccessDeniedHandler;
import com.shwotime.userservice.exception.CustomAuthenticationEntryPoint;
import com.shwotime.userservice.handler.CustomOAuthSuccessHandler;
import com.shwotime.userservice.service.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecuritConfig {

    private final CustomUserService customUserService;

    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String[] WHITE_LIST = {"/error","/fabicon.ico","/swagger-ui/**","/swagger-ui.html","/api-docs.html,'/oauth2/authorization/google","/user/register","/user/login"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request->request.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated())
                .oauth2Login(oauth2->oauth2
                        .userInfoEndpoint(userInfo->userInfo.userService(customUserService)).successHandler(customOAuthSuccessHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .exceptionHandling(exception->exception.accessDeniedHandler(customAccessDeniedHandler));


        return http.build();


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
