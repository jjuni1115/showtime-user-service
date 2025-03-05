package com.shwotime.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("showtime user API")
                .description("showtime user API")
                .version("0.0.1");

        return new OpenAPI()
                .info(info);

    }

}
