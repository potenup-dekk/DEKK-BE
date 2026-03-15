package com.dekk.deck.infrastructure.redis;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeckInviteRedisRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String DECK_KEY_PREFIX = "DECK_INVITE:";
    private static final String TOKEN_KEY_PREFIX = "INVITE_TOKEN:";

    private static final RedisScript<Long> SAVE_TOKEN_SCRIPT = RedisScript.of(
            "redis.call('SET', KEYS[1], ARGV[1], 'EX', ARGV[3]) "
                    + "redis.call('SET', KEYS[2], ARGV[2], 'EX', ARGV[3]) "
                    + "return 1",
            Long.class);

    public void saveToken(Long deckId, String token, Duration ttl) {
        String deckKey = DECK_KEY_PREFIX + deckId;
        String tokenKey = TOKEN_KEY_PREFIX + token;

        redisTemplate.execute(
                SAVE_TOKEN_SCRIPT,
                List.of(deckKey, tokenKey),
                token,
                String.valueOf(deckId),
                String.valueOf(ttl.getSeconds()));
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
