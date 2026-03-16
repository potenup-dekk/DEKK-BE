package com.dekk.deck.domain.repository;

import com.dekk.deck.domain.model.Deck;
import java.util.List;
import java.util.Optional;

public interface DeckRepository {
    Deck save(Deck deck);

    Optional<Deck> findDefaultDeckByUserId(Long userId);

    Optional<Deck> findByIdAndMemberUserId(Long id, Long userId);

    void delete(Deck deck);

    List<Deck> findCustomAndSharedDecksByUserIdOrderByCreatedAtDesc(Long userId);

    List<Deck> findAllByUserIdOrderByTypeAndCreatedAtDesc(Long userId);

    Optional<Deck> findById(Long id);
}
