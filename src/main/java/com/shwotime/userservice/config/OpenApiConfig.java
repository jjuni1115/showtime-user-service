package com.shwotime.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("http://172.16.111.164:8000/user-service")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Showtime User Service API")
                        .version("v0.0.1")
                        .description("showtime 유저 관련 API"));
    }

}
