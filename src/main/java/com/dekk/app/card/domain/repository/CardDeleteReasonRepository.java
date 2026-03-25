package com.dekk.app.card.domain.repository;

import com.dekk.app.card.domain.model.CardDeleteReason;

public interface CardDeleteReasonRepository {
    CardDeleteReason save(CardDeleteReason cardDeleteReason);
}
