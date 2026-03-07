package com.dekk.auth.infrastructure.redis;

import com.dekk.auth.domain.model.BlacklistAccessToken;
import com.dekk.auth.domain.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BlacklistRepositoryImpl implements BlacklistRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "BL:";

    @Override
    public void save(BlacklistAccessToken blacklistAccessToken, long remainingSeconds) {
        String key = PREFIX + blacklistAccessToken.getAccessToken();
        try {
            redisTemplate.opsForValue().set(key, blacklistAccessToken.getStatus(), remainingSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[Redis Fail-Safe] 블랙리스트 저장 실패 - Token: {}, Reason: {}", key, e.getMessage());
        }
    }

    @Override
    public boolean existsByAccessToken(String accessToken) {
        String key = PREFIX + accessToken;
        try {
            Boolean hasKey = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(hasKey);
        } catch (Exception e) {
            log.error("[Redis Fail-Open] 블랙리스트 조회 실패 - Token: {}, Reason: {}. 안전을 위해 false 반환", key, e.getMessage());
            return false;
        }
    }
}
