package com.shwotime.userservice.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    @PostMapping("/register")
    public Boolean createUserAccount(){
        return true;
    }


}
