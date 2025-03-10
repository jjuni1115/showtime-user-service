package com.shwotime.userservice.service;

import com.shwotime.userservice.dto.CustomUserDetailDto;
import com.shwotime.userservice.dto.OAuthAttributes;
import com.shwotime.userservice.endpoint.UserEndpoint;
import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.repository.UserRepository;
import com.shwotime.userservice.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String,Object> oauth2UserAttributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


        log.info(oauth2UserAttributes.get("name").toString());
        log.info(userRequest.getClientRegistration().getRegistrationId());

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId,oauth2UserAttributes);

        UserEntity userEntity = userRepository.findByEmail(oAuthAttributes.getEmail()).orElseGet(() -> {
            return UserEntity.builder()
                    .role(Role.GUSET)
                    .build();
        });


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoles())),
                oAuth2User.getAttributes(),userNameAttributeName
        );
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found"));


        return new CustomUserDetailDto(userEntity, Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoles())));


    }
}
