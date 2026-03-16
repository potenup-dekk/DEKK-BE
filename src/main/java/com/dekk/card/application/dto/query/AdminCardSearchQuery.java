package com.dekk.card.application.dto.query;

import com.dekk.card.domain.model.enums.CardStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record AdminCardSearchQuery(
        Long cardId,
        String originId,
        CardStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<Long> categoryIds) {

    public AdminCardSearchQuery(
            Long cardId,
            String originId,
            CardStatus status,
            LocalDate startDate,
            LocalDate endDate,
            List<Long> categoryIds) {
        this(
                cardId,
                originId,
                status,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(LocalTime.MAX) : null,
                categoryIds != null ? categoryIds.stream().distinct().toList() : null);
    }
}
