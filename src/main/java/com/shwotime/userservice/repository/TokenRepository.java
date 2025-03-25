package com.shwotime.userservice.repository;

import com.shwotime.userservice.redis.TokenRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<TokenRedis,String> {

    Optional<TokenRedis> findByUserEmail(String userEmail);

}
