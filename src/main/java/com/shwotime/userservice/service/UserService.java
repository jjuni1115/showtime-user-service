package com.shwotime.userservice.service;

import com.shwotime.userservice.dto.UserDto;
import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.repository.UserRepository;
import com.shwotime.userservice.type.Role;
import com.shwotime.userservice.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Boolean createUserAccount(UserDto req) {


        UserEntity userEntity = UserEntity.builder()
                .email(req.getUserEmail())
                .name(req.getUserName())
                .gender(req.getGender())
                .phoneNumber(req.getPhoneNumber())
                .password(passwordEncoder.encode(req.getUserPassword()))
                .role(Role.USER)
                .build();


        userRepository.saveAndFlush(userEntity);

        return true;
    }

    @Transactional
    public String userLogin(UserDto req) {
        UserEntity userEntity = userRepository.findByEmail(req.getUserEmail()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        if (passwordEncoder.matches(req.getUserPassword(), userEntity.getPassword())) {

            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletContainer.getResponse();


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEntity.getEmail(),userEntity.getPassword(), Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoles())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(userEntity.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getEmail());

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setSecure(false);
            cookie.setPath("/user/reissue");


            response.addCookie(cookie);
            return token;

        }else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

    }

}
