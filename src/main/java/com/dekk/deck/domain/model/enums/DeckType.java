package com.dekk.deck.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeckType {
    DEFAULT("기본덱"),
    CUSTOM("커스텀"),
    SHARED("쉐어덱");

    private final String description;
}
