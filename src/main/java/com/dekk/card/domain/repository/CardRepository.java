package com.dekk.card.domain.repository;

import com.dekk.card.application.dto.query.AdminCardSearchQuery;
import com.dekk.card.application.dto.query.RecommendCandidateQuery;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepository {
    Card save(Card card);

    List<Card> saveAll(List<Card> cards);

    boolean existsByPlatformAndOriginId(Platform platform, String originId);

    Page<Card> findCardsWithImageByStatus(CardStatus status, Pageable pageable);

    Page<Card> findCardsWithProductsByStatus(CardStatus status, Pageable pageable);

    List<Card> findAllByIdInWithProducts(List<Long> ids);

    List<Card> findRecommendCandidates(RecommendCandidateQuery query);

    Optional<Card> findById(Long id);

    Optional<Card> findByIdWithDetails(Long id);

    boolean existsById(Long id);

    Page<Card> searchCards(AdminCardSearchQuery condition, Pageable pageable);
}
