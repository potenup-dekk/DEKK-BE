package com.dekk.app.auth.domain.repository;

import com.dekk.app.auth.domain.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
