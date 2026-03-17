package com.dekk.deck.application;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckWithdrawalCommandService {

    private final DeckRepository deckRepository;
    private final DeckMemberRepository deckMemberRepository;
    private final DeckCardRepository deckCardRepository;

    private final CustomDeckCommandService customDeckCommandService;
    private final ShareDeckCommandService shareDeckCommandService;

    @Transactional
    public void processWithdrawal(Long userId) {
        List<Deck> joinedDecks = deckRepository.findAllByUserIdOrderByTypeAndCreatedAtDesc(userId);

        for (Deck deck : joinedDecks) {
            processDeckWithdrawal(userId, deck);
        }
    }

    private void processDeckWithdrawal(Long userId, Deck deck) {
        if (deck.isDefault()) {
            deleteDefaultDeck(deck.getId());
            return;
        }

        if (deck.isCustom()) {
            customDeckCommandService.deleteCustomDeck(userId, deck.getId());
            return;
        }

        if (deck.isShared()) {
            handleSharedDeckWithdrawal(userId, deck.getId());
        }
    }

    private void deleteDefaultDeck(Long deckId) {
        deckCardRepository.deleteAllByDeckId(deckId);
        deckMemberRepository.deleteAllByDeckId(deckId);
        deckRepository.findById(deckId).ifPresent(deckRepository::delete);
    }

    private void handleSharedDeckWithdrawal(Long userId, Long deckId) {
        deckMemberRepository
                .findByDeckIdAndUserId(deckId, userId)
                .ifPresent(member -> executeSharedDeckActionByRole(userId, deckId, member));
    }

    private void executeSharedDeckActionByRole(Long userId, Long deckId, DeckMember member) {
        if (member.isHost()) {
            shareDeckCommandService.handleHostWithdrawal(deckId, userId);
            return;
        }

        shareDeckCommandService.leaveSharedDeck(userId, deckId);
    }
}
