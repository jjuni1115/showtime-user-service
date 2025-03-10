package com.shwotime.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    private String userEmail;
    private String userPassword;
    private String userName;
    private String gender;
    private String age;
    private String phoneNumber;


}
