package com.dekk.app.deck.application;

import com.dekk.app.deck.domain.exception.DeckBusinessException;
import com.dekk.app.deck.domain.exception.DeckErrorCode;
import com.dekk.app.deck.domain.model.Deck;
import com.dekk.app.deck.domain.model.DeckCard;
import com.dekk.app.deck.domain.repository.DeckCardRepository;
import com.dekk.app.deck.domain.repository.DeckRepository;
import com.dekk.global.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckCardCommandService {

    private static final int MAX_CUSTOM_DECK_CARD_COUNT = 50;

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;

    @Transactional
    public void saveToDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = getDefaultDeckByUserId(userId);

        if (isCardAlreadyInDeck(defaultDeck.getId(), cardId)) {
            return;
        }

        deckCardRepository.save(DeckCard.create(defaultDeck.getId(), cardId));
    }

    @Transactional
    public void removeFromDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = getDefaultDeckByUserId(userId);
        DeckCard deckCard = getDeckCardByDeckIdAndCardId(defaultDeck.getId(), cardId);

        deckCardRepository.delete(deckCard);
    }

    /**
     * 분산 락 AOP 내부(AopForTransaction)에서 트랜잭션을 제어하므로 @Transactional을 생략합니다.
     * (동시성 정합성을 위해 '락 획득 -> 트랜잭션 시작 -> DB 커밋 -> 락 해제' 순서를 보장해야 함)
     */
    @DistributedLock(key = "#customDeckId")
    public void saveToCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = getCustomDeckByUserId(customDeckId, userId);

        if (isCardAlreadyInDeck(customDeck.getId(), cardId)) {
            return;
        }

        validateCustomDeckCardLimit(customDeck.getId());

        deckCardRepository.save(DeckCard.create(customDeck.getId(), cardId));
    }

    @Transactional
    public void removeFromCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = getCustomDeckByUserId(customDeckId, userId);
        DeckCard deckCard = getDeckCardByDeckIdAndCardId(customDeck.getId(), cardId);

        deckCardRepository.delete(deckCard);
    }

    private boolean isCardAlreadyInDeck(Long deckId, Long cardId) {
        return deckCardRepository.existsByDeckIdAndCardId(deckId, cardId);
    }

    private Deck getDefaultDeckByUserId(Long userId) {
        return deckRepository
                .findDefaultDeckByUserId(userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));
    }

    private Deck getCustomDeckByUserId(Long deckId, Long userId) {
        Deck deck = deckRepository
                .findByIdAndMemberUserId(deckId, userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        if (deck.isDefault()) {
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED);
        }

        return deck;
    }

    private DeckCard getDeckCardByDeckIdAndCardId(Long deckId, Long cardId) {
        return deckCardRepository
                .findByDeckIdAndCardId(deckId, cardId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CARD_NOT_FOUND_IN_DECK));
    }

    private void validateCustomDeckCardLimit(Long deckId) {
        long currentCardCount = deckCardRepository.countByDeckId(deckId);
        if (currentCardCount >= MAX_CUSTOM_DECK_CARD_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_CARD_LIMIT_EXCEEDED);
        }
    }
}
