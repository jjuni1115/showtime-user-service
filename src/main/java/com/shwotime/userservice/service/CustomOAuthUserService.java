package com.shwotime.userservice.service;

import com.shwotime.userservice.dto.OAuthAttributes;
import com.shwotime.userservice.entity.UserEntity;
import com.shwotime.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class CustomOAuthUserService extends DefaultOAuth2UserService {

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
                    .role("ROLE_GUEST")
                    .build();
        });


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole())),
                oAuth2User.getAttributes(),userNameAttributeName
        );
    }

}
