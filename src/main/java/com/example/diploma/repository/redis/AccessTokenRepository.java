package com.example.diploma.repository.redis;

import com.example.diploma.entity.redisEntity.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, String> {

    boolean existsByUsername(String username);

    boolean existsByToken(String token);

}
