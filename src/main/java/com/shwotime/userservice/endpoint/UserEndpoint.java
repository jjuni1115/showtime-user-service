package com.shwotime.userservice.endpoint;

import com.shwotime.userservice.dto.UserDto;
import com.shwotime.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;


    @PostMapping("/register")
    public Boolean createUserAccount(@RequestBody UserDto req){

        userService.createUserAccount(req);

        return true;

    }


    @PostMapping("/login")
    public String login(@RequestBody UserDto req){
        return userService.userLogin(req);
    }


}
