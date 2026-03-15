package com.dekk.deck.domain.model;

import com.dekk.common.entity.BaseTimeEntity;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.enums.DeckRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "deck_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE deck_members SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class DeckMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deck_id", nullable = false)
    private Long deckId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeckRole role;

    private DeckMember(Long deckId, Long userId, DeckRole role) {
        this.deckId = deckId;
        this.userId = userId;
        this.role = role;
    }

    public static DeckMember create(Long deckId, Long userId, DeckRole role) {
        if (deckId == null) {
            throw new DeckBusinessException(DeckErrorCode.DECK_ID_IS_REQUIRED);
        }
        if (userId == null) {
            throw new DeckBusinessException(DeckErrorCode.USER_ID_IS_REQUIRED_TO_CREATE);
        }
        if (role == null) {
            throw new DeckBusinessException(DeckErrorCode.DECK_ROLE_IS_REQUIRED);
        }
        return new DeckMember(deckId, userId, role);
    }

    public void promoteToHost() {
        this.role = DeckRole.HOST;
    }

    public boolean isHost() {
        return this.role == DeckRole.HOST;
    }
}
