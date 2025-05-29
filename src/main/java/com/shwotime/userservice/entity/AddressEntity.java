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
    private Long addressCode;

    private String addressNm;
    private String addressFullNm;
    private String addressEngNm;


}
