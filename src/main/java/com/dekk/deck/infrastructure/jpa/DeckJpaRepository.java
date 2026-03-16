package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.enums.DeckType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckJpaRepository extends JpaRepository<Deck, Long> {

    @Query("SELECT d FROM Deck d JOIN DeckMember dm ON d.id = dm.deckId "
            + "WHERE dm.userId = :userId AND d.deckType = :deckType "
            + "AND dm.deletedAt IS NULL AND d.deletedAt IS NULL")
    Optional<Deck> findByUserIdAndDeckType(@Param("userId") Long userId, @Param("deckType") DeckType deckType);

    @Query("SELECT d FROM Deck d JOIN DeckMember dm ON d.id = dm.deckId " + "WHERE d.id = :id AND dm.userId = :userId "
            + "AND dm.deletedAt IS NULL AND d.deletedAt IS NULL")
    Optional<Deck> findByIdAndMemberUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT d FROM Deck d JOIN DeckMember dm ON d.id = dm.deckId "
            + "WHERE dm.userId = :userId AND d.deckType != :deckType "
            + "AND dm.deletedAt IS NULL AND d.deletedAt IS NULL "
            + "ORDER BY d.createdAt DESC")
    List<Deck> findByUserIdAndDeckTypeNotOrderByCreatedAtDesc(
            @Param("userId") Long userId, @Param("deckType") DeckType deckType);

    @Query("SELECT d FROM Deck d JOIN DeckMember dm ON d.id = dm.deckId " + "WHERE dm.userId = :userId "
            + "AND dm.deletedAt IS NULL AND d.deletedAt IS NULL "
            + "ORDER BY CASE WHEN d.deckType = :defaultType THEN 0 ELSE 1 END, d.createdAt DESC")
    List<Deck> findAllByUserIdOrderByTypeAndCreatedAtDesc(
            @Param("userId") Long userId, @Param("defaultType") DeckType defaultType);
}
