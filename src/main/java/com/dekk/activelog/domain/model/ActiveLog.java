package com.dekk.activelog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ActiveLog {
    private final Long id;
    private final Long userId;
    private final Long cardId;
    private final SwipeType swipeType;

    @Builder
    public ActiveLog(Long id, Long userId, Long cardId, SwipeType swipeType) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.swipeType = swipeType;
    }

    public static ActiveLog create(Long userId, Long cardId, SwipeType swipeType) {
        return ActiveLog.builder()
            .userId(userId)
            .cardId(cardId)
            .swipeType(swipeType)
            .build();
    }
}
