package com.dekk.card.application.dto.query;

import com.dekk.card.domain.model.enums.ProductGender;

import java.util.List;

public record RecommendCandidateQuery(
        List<Long> excludedCardIds,
        List<ProductGender> genders,
        int minHeight,
        int maxHeight,
        int minWeight,
        int maxWeight
) {
    public List<Long> excludedCardIdsOrNull() {
        return excludedCardIds == null || excludedCardIds.isEmpty() ? null : excludedCardIds;
    }
}
