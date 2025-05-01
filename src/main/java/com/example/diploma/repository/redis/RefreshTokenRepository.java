package com.example.diploma.repository.redis;

import com.example.diploma.entity.redisEntity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {

    boolean existsByUsername(String username);

    boolean existsByToken(String token);

}
