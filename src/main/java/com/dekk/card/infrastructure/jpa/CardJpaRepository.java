package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<Card, Long> {
    boolean existsByPlatformAndOriginId(Platform platform, String originId);
}
