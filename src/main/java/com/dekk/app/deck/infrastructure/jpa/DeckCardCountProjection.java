package com.dekk.app.deck.infrastructure.jpa;

public interface DeckCardCountProjection {
    Long getDeckId();

    Long getCardCount();
}
