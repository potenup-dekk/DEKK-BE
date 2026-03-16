package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.enums.DeckType;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.jpa.DeckJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeckRepositoryImpl implements DeckRepository {

    private final DeckJpaRepository deckJpaRepository;

    @Override
    public Deck save(Deck deck) {
        return deckJpaRepository.save(deck);
    }

    @Override
    public Optional<Deck> findDefaultDeckByUserId(Long userId) {
        return deckJpaRepository.findByUserIdAndDeckType(userId, DeckType.DEFAULT);
    }

    @Override
    public Optional<Deck> findByIdAndMemberUserId(Long id, Long userId) {
        return deckJpaRepository.findByIdAndMemberUserId(id, userId);
    }

    @Override
    public void delete(Deck deck) {
        deckJpaRepository.delete(deck);
    }

    @Override
    public List<Deck> findCustomAndSharedDecksByUserIdOrderByCreatedAtDesc(Long userId) {
        return deckJpaRepository.findByUserIdAndDeckTypeNotOrderByCreatedAtDesc(userId, DeckType.DEFAULT);
    }

    @Override
    public List<Deck> findAllByUserIdOrderByTypeAndCreatedAtDesc(Long userId) {
        return deckJpaRepository.findAllByUserIdOrderByTypeAndCreatedAtDesc(userId, DeckType.DEFAULT);
    }

    @Override
    public Optional<Deck> findById(Long id) {
        return deckJpaRepository.findById(id);
    }
}
