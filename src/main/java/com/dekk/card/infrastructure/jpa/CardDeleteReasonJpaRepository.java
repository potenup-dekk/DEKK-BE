package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.CardDeleteReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardDeleteReasonJpaRepository extends JpaRepository<CardDeleteReason, Long> {}
