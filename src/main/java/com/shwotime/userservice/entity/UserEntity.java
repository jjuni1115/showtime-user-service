package com.shwotime.userservice.entity;

import com.shwotime.userservice.type.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "user")
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    private String nickName;
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String phoneNumber;
    private String gender;


}
