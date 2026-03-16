package com.dekk.deck.application.dto.result;

import com.dekk.card.application.dto.result.MemberCardResult;
import java.util.Collections;
import java.util.List;

public record MyDeckCardResult(
        Long cardId,
        String cardImageUrl,
        Integer height,
        Integer weight,
        List<String> tag,
        List<ProductDetail> products) {

    public static MyDeckCardResult empty(Long cardId) {
        return new MyDeckCardResult(cardId, null, null, null, Collections.emptyList(), Collections.emptyList());
    }

    public static MyDeckCardResult from(Long cardId, MemberCardResult cardInfo) {
        if (cardInfo == null) {
            return empty(cardId);
        }

        List<ProductDetail> productDetails = cardInfo.products().stream()
                .map(p -> new ProductDetail(p.brand(), p.productUrl(), p.name(), p.productImageUrl()))
                .toList();

        return new MyDeckCardResult(
                cardInfo.cardId(),
                cardInfo.cardImageUrl(),
                cardInfo.height(),
                cardInfo.weight(),
                cardInfo.tags(),
                productDetails);
    }

    public record ProductDetail(String brand, String url, String name, String productsImageUrl) {}
}
