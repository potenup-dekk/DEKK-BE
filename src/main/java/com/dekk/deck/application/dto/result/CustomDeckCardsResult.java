package com.dekk.deck.application.dto.result;

import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.model.enums.DeckType;
import java.util.List;

public record CustomDeckCardsResult(
        DeckType deckType, DeckRole role, String token, Long expiredInSeconds, List<MyDeckCardResult> cards) {
    public static CustomDeckCardsResult of(
            DeckType deckType, DeckRole role, String token, Long expiredInSeconds, List<MyDeckCardResult> cards) {
        return new CustomDeckCardsResult(deckType, role, token, expiredInSeconds, cards);
    }
}
