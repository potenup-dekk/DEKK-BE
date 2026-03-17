package com.dekk.deck.application.dto.result;

import com.dekk.deck.domain.model.enums.DeckType;
import java.util.List;

public record CustomDeckCardsResult(
        DeckType deckType, String token, Long expiredInSeconds, List<MyDeckCardResult> cards) {
    public static CustomDeckCardsResult of(
            DeckType deckType, String token, Long expiredInSeconds, List<MyDeckCardResult> cards) {
        return new CustomDeckCardsResult(deckType, token, expiredInSeconds, cards);
    }
}
