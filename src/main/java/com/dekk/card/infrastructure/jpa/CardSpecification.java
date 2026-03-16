package com.dekk.card.infrastructure.jpa;

import com.dekk.card.application.dto.query.AdminCardSearchQuery;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardCategory;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CardSpecification {

    private CardSpecification() {}

    public static Specification<Card> searchByCondition(AdminCardSearchQuery condition) {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                root.fetch("cardImage", JoinType.LEFT);
            }

            List<Predicate> predicates = new ArrayList<>();

            if (condition.cardId() != null) {
                predicates.add(cb.equal(root.get("id"), condition.cardId()));
            }

            if (condition.originId() != null && !condition.originId().isBlank()) {
                predicates.add(cb.equal(root.get("originId"), condition.originId()));
            }

            if (condition.status() != null) {
                predicates.add(cb.equal(root.get("status"), condition.status()));
            }

            if (condition.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), condition.startDate()));
            }

            if (condition.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), condition.endDate()));
            }

            if (condition.categoryIds() != null && !condition.categoryIds().isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<CardCategory> cardCategory = subquery.from(CardCategory.class);
                subquery.select(cardCategory.get("cardId"))
                        .where(
                                cb.equal(cardCategory.get("cardId"), root.get("id")),
                                cardCategory.get("categoryId").in(condition.categoryIds()),
                                cb.isNull(cardCategory.get("deletedAt")))
                        .groupBy(cardCategory.get("cardId"))
                        .having(cb.equal(cb.countDistinct(cardCategory.get("categoryId")), (long)
                                condition.categoryIds().size()));
                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
