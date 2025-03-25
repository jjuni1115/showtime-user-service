package com.shwotime.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address_info")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressNm;
    private String addressFullNm;
    private String addressCode;


}
