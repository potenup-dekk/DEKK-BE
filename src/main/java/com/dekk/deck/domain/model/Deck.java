package com.dekk.deck.domain.model;

import com.dekk.common.entity.BaseTimeEntity;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.enums.DeckType;
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
@Table(name = "decks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE decks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class Deck extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeckType deckType;

    private Deck(Long userId, String name, DeckType deckType) {
        this.userId = userId;
        this.name = name;
        this.deckType = deckType;
    }

    public boolean isShared() {
        return this.deckType == DeckType.SHARED;
    }

    public boolean isCustom() {
        return this.deckType == DeckType.CUSTOM;
    }

    public boolean isDefault() {
        return this.deckType == DeckType.DEFAULT;
    }

    public static Deck createDefault(Long userId) {
        String defaultName = "나의 기본 덱";
        validateEmpty(userId, defaultName);
        return new Deck(userId, defaultName, DeckType.DEFAULT);
    }

    public static Deck createCustom(Long userId, String name) {
        validateEmpty(userId, name);
        validateCustomNameLength(name);
        return new Deck(userId, name, DeckType.CUSTOM);
    }

    public void updateCustomName(String newName) {
        validateCustomModifiable();
        validateEmpty(this.userId, newName);
        validateCustomNameLength(newName);
        this.name = newName;
    }

    public void changeToShared() {
        validateCustomModifiable();
        this.deckType = DeckType.SHARED;
    }

    public void changeToCustom() {
        validateCustomModifiable();
        this.deckType = DeckType.CUSTOM;
    }

    public void deleteCustom() {
        validateCustomModifiable();
    }

    private static void validateEmpty(Long userId, String name) {
        if (userId == null) {
            throw new DeckBusinessException(DeckErrorCode.USER_ID_IS_REQUIRED_TO_CREATE);
        }
        if (name == null || name.isBlank()) {
            throw new DeckBusinessException(DeckErrorCode.DECK_NAME_IS_REQUIRED_TO_CREATE);
        }
    }

    private static void validateCustomNameLength(String name) {
        if (name.length() < 1 || name.length() > 15) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NAME_LENGTH_INVALID);
        }
    }

    private void validateCustomModifiable() {
        if (this.isDefault()) {
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED);
        }
    }
}
