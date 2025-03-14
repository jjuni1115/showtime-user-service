package com.shwotime.userservice.service;

import com.shwotime.userservice.dto.TokenDto;
import com.shwotime.userservice.dto.UserDto;
import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.exception.CustomRuntimeException;
import com.shwotime.userservice.redis.TokenRedis;
import com.shwotime.userservice.repository.TokenRepository;
import com.shwotime.userservice.repository.UserRepository;
import com.shwotime.userservice.type.ErrorCode;
import com.shwotime.userservice.type.Role;
import com.shwotime.userservice.util.CookieUtil;
import com.shwotime.userservice.util.JwtTokenProvider;
import com.shwotime.userservice.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    private final JwtUtil jwtUtil;

    private final TokenRepository tokenRepository;

    @Transactional
    public Boolean createUserAccount(UserDto req) {


        UserEntity userEntity = UserEntity.builder()
                .email(req.getUserEmail())
                .name(req.getUserName())
                .gender(req.getGender())
                .phoneNumber(req.getPhoneNumber())
                .age(req.getAge())
                .nickName(req.getNickName())
                .password(passwordEncoder.encode(req.getUserPassword()))
                .role(Role.USER)
                .build();


        userRepository.saveAndFlush(userEntity);

        return true;
    }

    @Transactional
    public TokenDto userLogin(UserDto req) {
        UserEntity userEntity = userRepository.findByEmail(req.getUserEmail()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        if (passwordEncoder.matches(req.getUserPassword(), userEntity.getPassword())) {

            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletContainer.getResponse();


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoles())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(userEntity.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getEmail());

            CookieUtil.createCookie(response,"refreshToken",refreshToken,"/user/reissueToken",60*60*24);

            TokenRedis tokenRedis = TokenRedis.builder()
                    .userEmail(userEntity.getEmail())
                    .refreshToken(refreshToken)
                    .build();

            tokenRepository.save(tokenRedis);

            TokenDto tokenDto = TokenDto.builder()
                    .token(token)
                    .build();

            return tokenDto;

        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

    }

    @Transactional
    public TokenDto reissueToken(String refreshToken) {


        ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletContainer.getResponse();
        String userEmail = jwtUtil.getUserEmail();


        if(refreshToken!=null && jwtTokenProvider.validateToken(refreshToken) && tokenRepository.findByUserEmail(userEmail).isPresent()){

            String token = jwtTokenProvider.generateToken(userEmail);
            String rotateRefreshToken = jwtTokenProvider.generateRefreshToken(userEmail);
            CookieUtil.deleteCookie(response,"refreshToken");
            CookieUtil.createCookie(response,"refreshToken",rotateRefreshToken,"/user/reissueToken",60*60*24);


            TokenRedis tokenRedis = TokenRedis.builder()
                    .userEmail(userEmail)
                    .refreshToken(rotateRefreshToken)
                    .build();
            tokenRepository.save(tokenRedis);


            TokenDto res = TokenDto.builder()
                    .token(token)
                    .build();
            return res;
        }else{
            throw new CustomRuntimeException(ErrorCode.TOKEN_EXPIRED_EXCEPTION);
        }



    }

}
