package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.CardDeleteReason;

public interface CardDeleteReasonRepository {
    CardDeleteReason save(CardDeleteReason cardDeleteReason);
}
