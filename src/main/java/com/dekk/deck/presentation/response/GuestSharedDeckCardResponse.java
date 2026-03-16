package com.dekk.deck.presentation.response;

import com.dekk.deck.application.dto.result.GuestSharedDeckCardResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "비회원 전용 쉐어덱 카드 프리뷰 응답 (상품 상세 정보 차단)")
public record GuestSharedDeckCardResponse(
        @Schema(description = "카드 ID", example = "1") Long cardId,
        @Schema(description = "카드 이미지 URL") String cardImageUrl,
        @Schema(description = "키(cm)", example = "175") Integer height,
        @Schema(description = "몸무게(kg)", example = "65") Integer weight,

        @Schema(description = "태그 목록", example = "[\"캐주얼\", \"스트릿\"]")
        List<String> tags) {
    public static GuestSharedDeckCardResponse from(GuestSharedDeckCardResult result) {
        return new GuestSharedDeckCardResponse(
                result.cardId(), result.cardImageUrl(), result.height(), result.weight(), result.tags());
    }
}
