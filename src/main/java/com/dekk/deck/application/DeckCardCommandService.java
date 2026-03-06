package com.dekk.deck.application;

import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeckCardCommandService {

    private static final int MAX_CUSTOM_DECK_CARD_COUNT = 50;

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;

    public void saveToDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = deckRepository.findByUserIdAndIsDefaultTrue(userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));

        if (deckCardRepository.existsByDeckIdAndCardId(defaultDeck.getId(), cardId)) {
            return;
        }

        deckCardRepository.save(DeckCard.create(defaultDeck.getId(), cardId));
    }

    public void removeFromDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = deckRepository.findByUserIdAndIsDefaultTrue(userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));

        DeckCard deckCard = deckCardRepository.findByDeckIdAndCardId(defaultDeck.getId(), cardId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CARD_NOT_FOUND_IN_DECK));

        deckCardRepository.delete(deckCard);
    }

    public void saveToCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = deckRepository.findByIdAndUserId(customDeckId, userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        if (deckCardRepository.existsByDeckIdAndCardId(customDeck.getId(), cardId)) {
            return;
        }

        long currentCardCount = deckCardRepository.countByDeckId(customDeck.getId());
        if (currentCardCount >= MAX_CUSTOM_DECK_CARD_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_CARD_LIMIT_EXCEEDED);
        }

        deckCardRepository.save(DeckCard.create(customDeck.getId(), cardId));
    }

    public void removeFromCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = deckRepository.findByIdAndUserId(customDeckId, userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        DeckCard deckCard = deckCardRepository.findByDeckIdAndCardId(customDeck.getId(), cardId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CARD_NOT_FOUND_IN_DECK));

        deckCardRepository.delete(deckCard);
    }
}
