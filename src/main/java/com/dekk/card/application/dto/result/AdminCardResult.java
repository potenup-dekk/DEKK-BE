package com.dekk.card.application.dto.result;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.model.enums.TargetGender;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record AdminCardResult(
        Long cardId,
        String originId,
        CardStatus status,
        Platform platform,
        TargetGender targetGender,
        Integer height,
        Integer weight,
        List<String> tags,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static AdminCardResult from(Card card) {
        return new AdminCardResult(
                card.getId(),
                card.getOriginId(),
                card.getStatus(),
                card.getPlatform(),
                card.getTargetGender(),
                card.getHeight(),
                card.getWeight(),
                parseTags(card.getTags()),
                card.getCardImage() != null ? card.getCardImage().getImageUrl() : null,
                card.getCreatedAt(),
                card.getUpdatedAt());
    }

    private static List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
