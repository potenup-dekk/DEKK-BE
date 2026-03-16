package com.dekk.deck.application;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultDeckCommandService {

    private final DeckRepository deckRepository;
    private final DeckMemberRepository deckMemberRepository;

    public void createDefaultDeck(Long userId) {
        if (deckRepository.findDefaultDeckByUserId(userId).isPresent()) {
            return;
        }

        Deck defaultDeck = Deck.createDefault(userId);
        deckRepository.save(defaultDeck);

        DeckMember hostMember = DeckMember.create(defaultDeck.getId(), userId, DeckRole.HOST);
        deckMemberRepository.save(hostMember);
    }
}
