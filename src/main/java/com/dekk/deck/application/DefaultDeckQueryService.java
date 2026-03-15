package com.dekk.deck.application;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.deck.application.dto.result.MyDeckCardResult;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultDeckQueryService {

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final CardQueryService cardQueryService;

    public Page<MyDeckCardResult> getMyDefaultDeckCards(Long userId, Pageable pageable) {
        Deck defaultDeck = deckRepository
                .findDefaultDeckByUserId(userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));

        Page<DeckCard> deckCards = deckCardRepository.findAllByDeckId(defaultDeck.getId(), pageable);

        List<Long> cardIds =
                deckCards.getContent().stream().map(DeckCard::getCardId).toList();

        List<MemberCardResult> cardResults = cardQueryService.getCardsByIds(cardIds);

        Map<Long, MemberCardResult> cardMap =
                cardResults.stream().collect(Collectors.toMap(MemberCardResult::cardId, Function.identity()));

        return deckCards.map(
                deckCard -> MyDeckCardResult.from(deckCard.getCardId(), cardMap.get(deckCard.getCardId())));
    }
}
