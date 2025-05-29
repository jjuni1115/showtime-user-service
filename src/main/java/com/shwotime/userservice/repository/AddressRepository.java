package com.shwotime.userservice.repository;

import com.shwotime.userservice.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findTop100ByAddressNmLikeOrAddressFullNmLikeOrderByAddressCode(String keyword1,String keyword2);


}
