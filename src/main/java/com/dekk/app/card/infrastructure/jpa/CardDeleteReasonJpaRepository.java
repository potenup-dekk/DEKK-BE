package com.dekk.app.card.infrastructure.jpa;

import com.dekk.app.card.domain.model.CardDeleteReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardDeleteReasonJpaRepository extends JpaRepository<CardDeleteReason, Long> {}
