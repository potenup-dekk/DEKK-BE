package com.dekk.card.domain.model;

import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_delete_reasons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardDeleteReason extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    private CardDeleteReason(Long cardId, Long adminId, String reason) {
        this.cardId = cardId;
        this.adminId = adminId;
        this.reason = reason;
    }

    public static CardDeleteReason create(Long cardId, Long adminId, String reason) {
        if (cardId == null) {
            throw new CardBusinessException(CardErrorCode.CARD_ID_IS_REQUIRED);
        }
        if (adminId == null) {
            throw new CardBusinessException(CardErrorCode.ADMIN_ID_IS_REQUIRED);
        }
        if (reason == null || reason.isBlank()) {
            throw new CardBusinessException(CardErrorCode.CARD_DELETE_REASON_IS_REQUIRED);
        }
        return new CardDeleteReason(cardId, adminId, reason);
    }
}
