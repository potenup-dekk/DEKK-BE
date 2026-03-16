package com.dekk.deck.application;

import com.dekk.deck.application.dto.command.CustomDeckCreateCommand;
import com.dekk.deck.application.dto.command.CustomDeckUpdateCommand;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomDeckCommandService {

    private static final int MAX_TOTAL_DECK_COUNT = 9;

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final DeckMemberRepository deckMemberRepository;

    public void createCustomDeck(Long userId, CustomDeckCreateCommand command) {
        validateDeckLimit(userId);

        Deck customDeck = Deck.createCustom(userId, command.name());
        deckRepository.save(customDeck);

        DeckMember hostMember = DeckMember.create(customDeck.getId(), userId, DeckRole.HOST);
        deckMemberRepository.save(hostMember);
    }

    public void updateCustomDeckName(Long userId, Long deckId, CustomDeckUpdateCommand command) {
        Deck deck = getDeckByUserId(deckId, userId);
        deck.updateCustomName(command.name());
    }

    public void deleteCustomDeck(Long userId, Long deckId) {
        Deck deck = getDeckByUserId(deckId, userId);
        deck.deleteCustom();

        deckCardRepository.deleteAllByDeckId(deck.getId());
        deckMemberRepository.deleteAllByDeckId(deck.getId());
        deckRepository.delete(deck);
    }

    private void validateDeckLimit(Long userId) {
        long currentCount = deckMemberRepository.countByUserId(userId);
        if (currentCount >= MAX_TOTAL_DECK_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.DECK_LIMIT_EXCEEDED);
        }
    }

    private Deck getDeckByUserId(Long deckId, Long userId) {
        return deckRepository
                .findByIdAndMemberUserId(deckId, userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));
    }
}
