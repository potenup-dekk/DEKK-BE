package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckMemberJpaRepository extends JpaRepository<DeckMember, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE DeckMember dm SET dm.deletedAt = CURRENT_TIMESTAMP "
            + "WHERE dm.deckId = :deckId AND dm.deletedAt IS NULL")
    void deleteAllByDeckId(Long deckId);

    @Query("SELECT COUNT(dm) FROM DeckMember dm WHERE dm.userId = :userId AND dm.deletedAt IS NULL")
    long countByUserId(Long userId);

    Optional<DeckMember> findByDeckIdAndUserId(Long deckId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE DeckMember dm SET dm.deletedAt = CURRENT_TIMESTAMP "
            + "WHERE dm.deckId = :deckId AND dm.role = :role")
    void deleteAllGuestsByDeckId(@Param("deckId") Long deckId, @Param("role") DeckRole role);
}
