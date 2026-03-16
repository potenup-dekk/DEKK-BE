package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.DeckCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckCardJpaRepository extends JpaRepository<DeckCard, Long> {
    boolean existsByDeckIdAndCardId(Long deckId, Long cardId);

    Page<DeckCard> findAllByDeckId(Long deckId, Pageable pageable);

    Optional<DeckCard> findByDeckIdAndCardId(Long deckId, Long CardId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE DeckCard dc SET dc.deletedAt = CURRENT_TIMESTAMP WHERE dc.deckId = :deckId AND dc.deletedAt IS NULL")
    void deleteAllByDeckId(@Param("deckId") Long deckId);

    @Query("""
         SELECT d.deckId AS deckId, COUNT(d) AS cardCount
         FROM DeckCard d
         WHERE d.deckId IN :deckIds AND d.deletedAt IS NULL
         GROUP BY d.deckId
        """)
    List<DeckCardCountProjection> countCardsByDeckIds(@Param("deckIds") List<Long> deckIds);

    @Query("SELECT COUNT(dc) FROM DeckCard dc WHERE dc.deckId = :deckId AND dc.deletedAt IS NULL")
    long countByDeckId(@Param("deckId") Long deckId);

    @Query(value = """
        SELECT * FROM (
            SELECT dc.*, ROW_NUMBER() OVER(PARTITION BY dc.deck_id ORDER BY dc.created_at DESC) as rn
            FROM deck_cards dc
            WHERE dc.deck_id IN (:deckIds) AND dc.deleted_at IS NULL
        ) sub
        WHERE sub.rn <= :limit
        ORDER BY sub.deck_id, sub.created_at DESC
        """, nativeQuery = true)
    List<DeckCard> findTopCardsByDeckIdsIn(@Param("deckIds") List<Long> deckIds, @Param("limit") int limit);

    List<DeckCard> findAllByDeckIdOrderByCreatedAtDesc(Long deckId);
}
