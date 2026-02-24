package com.dekk.activelog.infrastructure.persistence.repository;

import com.dekk.activelog.infrastructure.persistence.entity.ActiveLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ActiveLogJpaRepository extends JpaRepository<ActiveLogEntity, Long> {
    boolean existsByUserIdAndCardId(Long userId, Long cardId);
    Optional<ActiveLogEntity> findByUserIdAndCardId(Long userId, Long cardId);
}
