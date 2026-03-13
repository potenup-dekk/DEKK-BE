package com.dekk.card.application.dto.query;

import com.dekk.card.domain.model.enums.CardStatus;
import java.time.LocalDateTime;

public record AdminCardSearchQuery(
        Long cardId, String originId, CardStatus status, LocalDateTime startDate, LocalDateTime endDate) {}
