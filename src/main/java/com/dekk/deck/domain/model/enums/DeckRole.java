package com.dekk.deck.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeckRole {
    HOST("호스트"),
    GUEST("게스트");

    private final String description;
}
