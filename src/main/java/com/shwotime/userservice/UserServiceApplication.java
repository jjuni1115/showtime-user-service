package com.shwotime.userservice;

import com.showtime.coreapi.type.ErrorCodeRegistry;
import com.shwotime.userservice.type.UserErrorCode;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void init(){

        for(UserErrorCode errorCode : UserErrorCode.values()){
            ErrorCodeRegistry.registerErrorCode(errorCode);
        }

    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
