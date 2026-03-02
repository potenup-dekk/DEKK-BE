package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardRepository {
    Card save(Card card);
    List<Card> saveAll(List<Card> cards);
    boolean existsByPlatformAndOriginId(Platform platform, String originId);
    Page<Card> findActiveCardsWithImage(Pageable pageable);
    Page<Card> findActiveCardsWithProducts(Pageable pageable);
    List<Card> findAllByIdInWithProducts(List<Long> ids);
}
