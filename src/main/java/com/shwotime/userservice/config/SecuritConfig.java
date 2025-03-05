package com.shwotime.userservice.config;

import com.shwotime.userservice.handler.CustomOAuthSuccessHandler;
import com.shwotime.userservice.service.CustomOAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecuritConfig {

    private final CustomOAuthUserService customOAuthUserService;

    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;

    private final String[] WHITE_LIST = {"/error","/fabicon.ico","/swagger-ui/**","/swagger-ui.html","/api-docs.html,'/oauth2/authorization/google"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated())
                .oauth2Login(oauth2->oauth2
                        .userInfoEndpoint(userInfo->userInfo.userService(customOAuthUserService)).successHandler(customOAuthSuccessHandler));


        return http.build();


    }

}
