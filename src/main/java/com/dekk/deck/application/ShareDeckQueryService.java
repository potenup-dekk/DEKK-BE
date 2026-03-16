package com.dekk.deck.application;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.deck.application.dto.result.GuestSharedDeckCardResult;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.redis.DeckInviteRedisRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShareDeckQueryService {

    private final DeckInviteRedisRepository deckInviteRedisRepository;
    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final CardQueryService cardQueryService;

    public List<GuestSharedDeckCardResult> getSharedDeckPreview(String token) {
        Deck deck = validateAndGetSharedDeck(token);

        List<DeckCard> deckCards = deckCardRepository.findAllByDeckIdOrderByCreatedAtDesc(deck.getId());
        if (deckCards.isEmpty()) {
            return List.of();
        }

        List<Long> cardIds = deckCards.stream().map(DeckCard::getCardId).toList();
        List<MemberCardResult> cardResults = cardQueryService.getCardsByIds(cardIds);

        return mapToGuestResults(deckCards, cardResults);
    }

    private Deck validateAndGetSharedDeck(String token) {
        Long deckId = deckInviteRedisRepository
                .getDeckIdByToken(token)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.SHARE_TOKEN_EXPIRED));

        Deck deck = deckRepository
                .findById(deckId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        if (!deck.isShared()) {
            throw new DeckBusinessException(DeckErrorCode.DECK_IS_NOT_SHARED);
        }

        return deck;
    }

    private List<GuestSharedDeckCardResult> mapToGuestResults(
            List<DeckCard> deckCards, List<MemberCardResult> cardResults) {

        Map<Long, MemberCardResult> cardMap =
                cardResults.stream().collect(Collectors.toMap(MemberCardResult::cardId, Function.identity()));

        return deckCards.stream()
                .map(deckCard -> cardMap.get(deckCard.getCardId()))
                .filter(Objects::nonNull)
                .map(this::createGuestResult)
                .toList();
    }

    private GuestSharedDeckCardResult createGuestResult(MemberCardResult card) {
        return new GuestSharedDeckCardResult(
                card.cardId(), card.cardImageUrl(), card.height(), card.weight(), card.tags());
    }
}
