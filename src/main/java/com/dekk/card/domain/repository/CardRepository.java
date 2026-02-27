package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.Platform;

import java.util.List;

public interface CardRepository {
    Card save(Card card);
    List<Card> saveAll(List<Card> cards);
    boolean existsByPlatformAndOriginId(Platform platform, String originId);
}
