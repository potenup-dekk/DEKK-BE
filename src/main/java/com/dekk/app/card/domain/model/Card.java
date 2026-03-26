package com.dekk.app.card.domain.model;

import com.dekk.app.card.application.dto.command.CardCreateCommand;
import com.dekk.app.card.application.dto.command.CardImageCreateCommand;
import com.dekk.app.card.application.dto.command.ProductCreateCommand;
import com.dekk.app.card.domain.exception.CardBusinessException;
import com.dekk.app.card.domain.exception.CardErrorCode;
import com.dekk.app.card.domain.model.enums.CardStatus;
import com.dekk.app.card.domain.model.enums.Platform;
import com.dekk.app.card.domain.model.enums.TargetGender;
import com.dekk.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "cards")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CardImage cardImage;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardProduct> cardProducts = new ArrayList<>();

    @Column(name = "tags")
    private String tags;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "origin_id", updatable = false)
    private String originId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Enumerated(EnumType.STRING)
    @Column
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_gender")
    private TargetGender targetGender;

    private Integer height;

    private Integer weight;

    private Card(
            CardImage cardImage,
            String tags,
            Long userId,
            String originId,
            Platform platform,
            TargetGender targetGender,
            Integer height,
            Integer weight) {
        this.cardImage = cardImage;
        this.tags = tags;
        this.userId = userId;
        this.originId = originId;
        this.status = CardStatus.PENDING;
        this.platform = platform;
        this.targetGender = targetGender;
        this.height = height;
        this.weight = weight;
    }

    public static Card create(CardCreateCommand command) {
        if (command.originId() == null) {
            throw new CardBusinessException(CardErrorCode.CARD_ORIGIN_ID_IS_REQUIRED_TO_CREATE);
        }

        if (command.platform() == null) {
            throw new CardBusinessException(CardErrorCode.CARD_PLATFORM_IS_REQUIRED_TO_CREATE);
        }

        CardImage cardImage = CardImage.create(command.cardImage());

        Card card = new Card(
                cardImage,
                command.tags(),
                null,
                command.originId(),
                command.platform(),
                command.targetGender(),
                command.height(),
                command.weight());

        cardImage.setCard(card);
        command.productCreateCommands().stream()
                .map(Product::create)
                .map(product -> CardProduct.create(card, product))
                .forEach(card.cardProducts::add);
        return card;
    }

    public static Card createByUser(
            Long userId,
            CardImageCreateCommand cardImageCommand,
            List<ProductCreateCommand> productCommands,
            String tags,
            Integer height,
            Integer weight,
            TargetGender targetGender) {
        if (userId == null) {
            throw new CardBusinessException(CardErrorCode.CARD_USER_ID_IS_REQUIRED_TO_CREATE);
        }

        CardImage cardImage = CardImage.create(cardImageCommand);

        Card card = new Card(cardImage, tags, userId, null, null, targetGender, height, weight);

        cardImage.setCard(card);
        productCommands.stream()
                .map(Product::create)
                .map(product -> CardProduct.create(card, product))
                .forEach(card.cardProducts::add);
        return card;
    }

    public void approve() {
        validateStatusChangeable();
        this.status = CardStatus.APPROVED;
    }

    public void reject() {
        validateStatusChangeable();
        this.status = CardStatus.REJECTED;
    }

    public void requestDelete() {
        validateStatusChangeable();
        this.status = CardStatus.DELETE_REQUESTED;
    }

    private void validateStatusChangeable() {
        if (!this.status.canChangeStatus()) {
            throw new CardBusinessException(CardErrorCode.CANNOT_CHANGE_STATUS_OF_DELETE_REQUESTED);
        }
    }
}
