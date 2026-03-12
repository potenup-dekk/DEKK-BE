package com.dekk.card.infrastructure.jpa;

import com.dekk.card.application.dto.query.AdminCardSearchQuery;
import com.dekk.card.domain.model.Card;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CardSpecification {

    private CardSpecification() {}

    public static Specification<Card> searchByCondition(AdminCardSearchQuery condition) {
        return (root, query, cb) -> {
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

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
