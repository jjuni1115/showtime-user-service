package com.shwotime.userservice.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN, ROLE_USER"),
    GUSET("ROLE_GUEST");

    private final String roles;

}
