package com.shwotime.userservice.endpoint;

import com.shwotime.userservice.common.ApiResponse;
import com.shwotime.userservice.dto.TokenDto;
import com.shwotime.userservice.dto.UserDto;
import com.shwotime.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final HttpServletRequest httpServletRequest;

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> createUserAccount(@RequestBody UserDto req){

        userService.createUserAccount(req);

        return ResponseEntity.ok(ApiResponse.ok("회원가입 성공",httpServletRequest.getRequestURI()));

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody UserDto req){
        String token = userService.userLogin(req);

        TokenDto res = new TokenDto().builder()
                .token(token)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(res,httpServletRequest.getRequestURI()));
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }


}
