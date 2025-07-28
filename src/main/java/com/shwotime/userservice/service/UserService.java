package com.shwotime.userservice.service;

import com.showtime.coreapi.exception.CustomRuntimeException;
import com.shwotime.userservice.dto.TokenDto;
import com.shwotime.userservice.dto.UserDto;
import com.shwotime.userservice.dto.UserInfo;
import com.shwotime.userservice.entity.AddressEntity;
import com.shwotime.userservice.entity.UserAddressEntity;
import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.redis.TokenRedis;
import com.shwotime.userservice.repository.AddressRepository;
import com.shwotime.userservice.repository.TokenRepository;
import com.shwotime.userservice.repository.UserAddressRepository;
import com.shwotime.userservice.repository.UserRepository;
import com.shwotime.userservice.type.Role;
import com.shwotime.userservice.type.UserErrorCode;
import com.shwotime.userservice.util.CookieUtil;
import com.shwotime.userservice.util.JwtTokenProvider;
import com.shwotime.userservice.util.JwtUtil;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtUtil jwtUtil;

    private final TokenRepository tokenRepository;
    private final AddressRepository addressRepository;

    private final UserAddressRepository userAddressRepository;


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



        UserEntity userSaveEntity = userRepository.saveAndFlush(userEntity);

        if(req.getAddressList()!=null){
            List<UserAddressEntity> userAddress = req.getAddressList().stream().map(address -> {

                Optional<AddressEntity> addressEntity = addressRepository.findById(Long.valueOf(address));

                UserAddressEntity userAddressEntity = UserAddressEntity
                        .builder()
                        .userEntity(userSaveEntity)
                        .addressId(addressEntity.get())
                        .build();
                return userAddressEntity;
            }).collect(Collectors.toList());


            userAddressRepository.saveAll(userAddress);
        }



        return true;
    }

    @Transactional
    public TokenDto userLogin(UserDto req) {
        UserEntity userEntity = userRepository.findByEmail(req.getUserEmail()).orElseThrow(() -> new CustomRuntimeException(UserErrorCode.USER_NOT_FOUND_EXCEPTION));
        if (passwordEncoder.matches(req.getUserPassword(), userEntity.getPassword())) {

            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletContainer.getResponse();


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoles())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(userEntity.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getEmail());

            CookieUtil.createCookie(response, "refreshToken",  "6ec3-218-145-133-193.ngrok-free.app", refreshToken, "/user/reissueToken", 60 * 60 * 24);

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


        if (refreshToken != null && jwtTokenProvider.validateTokenSignature(refreshToken) && tokenRepository.findByUserEmail(userEmail).isPresent()) {

            String token = jwtTokenProvider.generateToken(userEmail);
            String rotateRefreshToken = jwtTokenProvider.generateRefreshToken(userEmail);
            CookieUtil.deleteCookie(response, "refreshToken");
            CookieUtil.createCookie(response, "refreshToken", "https://6ec3-218-145-133-193.ngrok-free.app",rotateRefreshToken, "/user/reissueToken", 60 * 60 * 24);


            TokenRedis tokenRedis = TokenRedis.builder()
                    .userEmail(userEmail)
                    .refreshToken(rotateRefreshToken)
                    .build();
            tokenRepository.save(tokenRedis);


            TokenDto res = TokenDto.builder()
                    .token(token)
                    .build();
            return res;
        } else {
            throw new CustomRuntimeException(UserErrorCode.TOKEN_EXPIRED_EXCEPTION);
        }


    }

    @Transactional
    public Boolean logout() {


        ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletContainer.getResponse();

        String userEmail = jwtUtil.getUserEmail();
        tokenRepository.deleteById(userEmail);

        CookieUtil.deleteCookie(response, "refreshToken");
        return true;
    }

    @Transactional
    public String getUserPassport(){
        return null;

    }

    @Transactional
    public UserInfo getUserInfo(){

        UserInfo userInfo = new UserInfo();

        userInfo.setUserEmail(jwtUtil.getUserEmail());
        userInfo.setUserName(jwtUtil.getUserName());
        userInfo.setNickName(jwtUtil.getUserNickName());

        return userInfo;
    }

}
