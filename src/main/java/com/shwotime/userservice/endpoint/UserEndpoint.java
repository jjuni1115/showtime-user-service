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
        TokenDto res = userService.userLogin(req);


        return ResponseEntity.ok(ApiResponse.ok(res,httpServletRequest.getRequestURI()));
    }


    @PostMapping("/reissueToken")
    public ResponseEntity<ApiResponse<TokenDto>> reissueToken(@CookieValue(value = "refreshToken") String refreshToken){
        TokenDto token = userService.reissueToken(refreshToken);


        return ResponseEntity.ok(ApiResponse.ok(token,httpServletRequest.getRequestURI()));
    }



    //logout api delete refresh token in cookie
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(){
        Boolean res = userService.logout();
        return ResponseEntity.ok(ApiResponse.ok(res,httpServletRequest.getRequestURI()));
    }


    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String >> test(){
        return ResponseEntity.ok(ApiResponse.ok("test",httpServletRequest.getRequestURI()));
    }


}
