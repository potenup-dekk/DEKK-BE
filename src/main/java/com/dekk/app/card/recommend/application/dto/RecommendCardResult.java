package com.dekk.app.card.recommend.application.dto;

import com.dekk.app.card.application.dto.result.MemberCardResult;

/**
 * 추천 피드 단건 결과.
 * recommended=true  → 알고리즘이 선택한 추천 카드
 * recommended=false → 추천 소진 후 최신순으로 보충된 일반 카드
 */
public record RecommendCardResult(MemberCardResult card, boolean recommended) {

    public static RecommendCardResult recommended(MemberCardResult card) {
        return new RecommendCardResult(card, true);
    }

    public static RecommendCardResult normal(MemberCardResult card) {
        return new RecommendCardResult(card, false);
    }
}
