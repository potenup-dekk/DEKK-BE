package com.dekk.app.deck.infrastructure;

import com.dekk.app.deck.domain.model.DeckMember;
import com.dekk.app.deck.domain.model.enums.DeckRole;
import com.dekk.app.deck.domain.repository.DeckMemberRepository;
import com.dekk.app.deck.infrastructure.jpa.DeckMemberJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeckMemberRepositoryImpl implements DeckMemberRepository {

    private final DeckMemberJpaRepository jpaRepository;

    @Override
    public DeckMember save(DeckMember deckMember) {
        return jpaRepository.save(deckMember);
    }

    @Override
    public void deleteAllByDeckId(Long deckId) {
        jpaRepository.deleteAllByDeckId(deckId);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public Optional<DeckMember> findByDeckIdAndUserId(Long deckId, Long userId) {
        return jpaRepository.findByDeckIdAndUserId(deckId, userId);
    }

    @Override
    public void deleteAllGuestsByDeckId(Long deckId, DeckRole role) {
        jpaRepository.deleteAllGuestsByDeckId(deckId, role);
    }

    @Override
    public long countByDeckIdAndRole(Long deckId, DeckRole role) {
        return jpaRepository.countByDeckIdAndRole(deckId, role);
    }

    @Override
    public Optional<DeckMember> findFirstByDeckIdAndRoleOrderByCreatedAtAsc(Long deckId, DeckRole role) {
        return jpaRepository.findFirstByDeckIdAndRoleOrderByCreatedAtAsc(deckId, role);
    }

    @Override
    public void delete(DeckMember deckMember) {
        jpaRepository.delete(deckMember);
    }

    @Override
    public void reactivateOrSave(Long deckId, Long userId, DeckRole role) {
        int updatedCount = jpaRepository.reactivateIfDeleted(deckId, userId, role.name());

        if (updatedCount == 0) {
            jpaRepository.save(DeckMember.create(deckId, userId, role));
        }
    }
}
