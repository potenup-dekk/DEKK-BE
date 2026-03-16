package com.dekk.deck.application;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.deck.application.dto.result.DeckResult;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeckQueryService {

    private static final int MAX_PREVIEW_CARD_COUNT = 3;

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final CardQueryService cardQueryService;

    public List<DeckResult> getDecks(Long userId) {
        List<Deck> decks = deckRepository.findAllByUserIdOrderByTypeAndCreatedAtDesc(userId);

        if (decks.isEmpty()) {
            return List.of();
        }

        List<Long> deckIds = decks.stream().map(Deck::getId).toList();
        Map<Long, Long> cardCountMap = deckCardRepository.countCardsByDeckIds(deckIds);

        List<DeckCard> topDeckCards = deckCardRepository.findTopCardsByDeckIdsIn(deckIds, MAX_PREVIEW_CARD_COUNT);

        Map<Long, List<Long>> topCardIdsPerDeck = extractTopCardIdsPerDeck(topDeckCards);
        Map<Long, String> cardImageUrlMap = getCardImageUrlMap(topCardIdsPerDeck);

        return decks.stream()
                .map(deck -> mapToDeckResult(deck, cardCountMap, topCardIdsPerDeck, cardImageUrlMap))
                .toList();
    }

    private Map<Long, List<Long>> extractTopCardIdsPerDeck(List<DeckCard> topDeckCards) {
        return topDeckCards.stream()
                .collect(Collectors.groupingBy(
                        DeckCard::getDeckId, Collectors.mapping(DeckCard::getCardId, Collectors.toList())));
    }

    private Map<Long, String> getCardImageUrlMap(Map<Long, List<Long>> topCardIdsPerDeck) {
        List<Long> allTopCardIds = topCardIdsPerDeck.values().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        if (allTopCardIds.isEmpty()) {
            return Map.of();
        }

        List<MemberCardResult> cardResults = cardQueryService.getCardsByIds(allTopCardIds);

        return cardResults.stream()
                .filter(card -> card.cardImageUrl() != null)
                .collect(Collectors.toMap(MemberCardResult::cardId, MemberCardResult::cardImageUrl));
    }

    private DeckResult mapToDeckResult(
            Deck deck,
            Map<Long, Long> cardCountMap,
            Map<Long, List<Long>> topCardIdsPerDeck,
            Map<Long, String> cardImageUrlMap) {

        List<String> previewUrls = topCardIdsPerDeck.getOrDefault(deck.getId(), List.of()).stream()
                .map(cardId -> cardImageUrlMap.getOrDefault(cardId, ""))
                .filter(url -> !url.isBlank())
                .toList();

        return new DeckResult(
                deck.getId(),
                deck.getName(),
                deck.getDeckType(),
                cardCountMap.getOrDefault(deck.getId(), 0L),
                previewUrls);
    }
}
