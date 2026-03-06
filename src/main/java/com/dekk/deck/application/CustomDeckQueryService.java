package com.dekk.deck.application;

import com.dekk.deck.application.dto.result.CustomDeckResult;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomDeckQueryService {

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;

    public List<CustomDeckResult> getMyCustomDecks(Long userId) {
        List<Deck> myCustomDecks = deckRepository.findAllByUserIdAndIsDefaultFalseOrderByCreatedAtDesc(userId);

        if (myCustomDecks.isEmpty()) {
            return List.of();
        }

        List<Long> deckIds = myCustomDecks.stream()
            .map(Deck::getId)
            .toList();

        Map<Long, Long> cardCountMap = deckCardRepository.countCardsByDeckIds(deckIds);

        return myCustomDecks.stream()
            .map(deck -> CustomDeckResult.of(
                deck.getId(),
                deck.getName(),
                cardCountMap.getOrDefault(deck.getId(), 0L)
            ))
            .toList();
    }
}
