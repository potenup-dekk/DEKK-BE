package com.dekk.deck.infrastructure.redis;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeckInviteRedisRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String DECK_KEY_PREFIX = "DECK_INVITE:";
    private static final String TOKEN_KEY_PREFIX = "INVITE_TOKEN:";

    public void saveToken(Long deckId, String token, Duration ttl) {
        String deckKey = DECK_KEY_PREFIX + deckId;
        String tokenKey = TOKEN_KEY_PREFIX + token;

        redisTemplate.opsForValue().set(deckKey, token, ttl);
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(deckId), ttl);
    }

    public Optional<String> getTokenByDeckId(Long deckId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(DECK_KEY_PREFIX + deckId));
    }

    public Optional<Long> getDeckIdByToken(String token) {
        String deckIdStr = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + token);
        if (deckIdStr == null) {
            return Optional.empty();
        }
        return Optional.of(Long.valueOf(deckIdStr));
    }

    public long getRemainingSeconds(Long deckId) {
        Long expire = redisTemplate.getExpire(DECK_KEY_PREFIX + deckId);
        return expire != null && expire > 0 ? expire : 0L;
    }

    public void deleteTokens(Long deckId, String currentToken) {
        List<String> keysToDelete = new java.util.ArrayList<>();
        keysToDelete.add(DECK_KEY_PREFIX + deckId);

        if (currentToken != null) {
            keysToDelete.add(TOKEN_KEY_PREFIX + currentToken);
        }

        redisTemplate.delete(keysToDelete);
    }
}
