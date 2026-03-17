package com.dekk.deck.application.dto.result;

import com.dekk.deck.domain.model.enums.DeckType;

public record CustomDeckResult(Long deckId, String name, DeckType deckType, Long cardCount, String imageUrl) {
    public static CustomDeckResult of(Long deckId, String name, DeckType deckType, Long cardCount, String imageUrl) {
        return new CustomDeckResult(deckId, name, deckType, cardCount, imageUrl);
    }
}
