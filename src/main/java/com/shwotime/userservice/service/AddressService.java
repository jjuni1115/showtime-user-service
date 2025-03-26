package com.shwotime.userservice.service;

import com.shwotime.userservice.dto.AddressDto;
import com.shwotime.userservice.entity.AddressEntity;
import com.shwotime.userservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;



    @Transactional
    public List<AddressDto> searchAddress(String keyword){

        List<AddressEntity> addressEntities = addressRepository.findTop100ByAddressNmLikeOrAddressFullNmLikeOrderByAddressCode("%"+keyword+"%","%"+keyword+"%");
        System.out.println(keyword);

        return addressEntities.stream().map(addressEntity -> {
            AddressDto addressDto = new AddressDto();
            addressDto.setId(addressEntity.getAddressCode());
            addressDto.setAddress(addressEntity.getAddressFullNm());
            return addressDto;
        }).collect(Collectors.toList());

    }


}
