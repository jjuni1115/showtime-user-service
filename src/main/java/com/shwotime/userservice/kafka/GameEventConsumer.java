package com.shwotime.userservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventConsumer {

    @KafkaListener(topics = "game-event", groupId = "user-service-group")
    public void consumeGameEvent(String message) {

        System.out.println("Received game event: " + message);


    }

}
