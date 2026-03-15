package com.dekk.deck.domain.repository;

import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import java.util.Optional;

public interface DeckMemberRepository {
    DeckMember save(DeckMember deckMember);

    void deleteAllByDeckId(Long deckId);

    long countByUserId(Long userId);

    Optional<DeckMember> findByDeckIdAndUserId(Long deckId, Long userId);

    void deleteAllGuestsByDeckId(Long deckId, DeckRole role);

    long countByDeckIdAndRole(Long deckId, DeckRole role);

    Optional<DeckMember> findFirstByDeckIdAndRoleOrderByCreatedAtAsc(Long deckId, DeckRole role);

    void delete(DeckMember deckMember);
}
