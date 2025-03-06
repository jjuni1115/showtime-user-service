package com.shwotime.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String name;
    private String email;
    private String pictrueUrl;

    @Builder
    public OAuthAttributes(String name, String email, String pictureUrl){
        this.name = name;
        this.email = email;
        this.pictrueUrl = pictureUrl;
    }

    public static OAuthAttributes of(String registrationId, Map<String,Object> attributes){

        switch (registrationId){
            case "google" -> {
                return ofGoogle(attributes);
            }
            case "naver" ->{
                return null;
            }
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider");

        }

    }

    public static OAuthAttributes ofGoogle(Map<String,Object> attributes){
        return OAuthAttributes.builder()
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .pictureUrl(attributes.get("picture").toString())
                .build();
    }



}
