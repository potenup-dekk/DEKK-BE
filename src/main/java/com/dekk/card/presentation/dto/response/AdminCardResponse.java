package com.dekk.card.presentation.dto.response;

import com.dekk.card.application.dto.result.AdminCardResult;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.model.enums.TargetGender;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "관리자 카드 응답")
public record AdminCardResponse(
        @Schema(description = "카드 ID", example = "1") Long cardId,
        @Schema(description = "원본 ID", example = "abc123") String originId,
        @Schema(description = "카드 상태", example = "PENDING") CardStatus status,
        @Schema(description = "플랫폼", example = "MUSINSA") Platform platform,
        @Schema(description = "타겟 성별", example = "MALE") TargetGender targetGender,
        @Schema(description = "키(cm)", example = "175") Integer height,
        @Schema(description = "몸무게(kg)", example = "65") Integer weight,

        @Schema(description = "태그 목록", example = "[\"캐주얼\", \"스트릿\"]")
        List<String> tags,

        @Schema(description = "생성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt) {

    public static AdminCardResponse from(AdminCardResult result) {
        return new AdminCardResponse(
                result.cardId(),
                result.originId(),
                result.status(),
                result.platform(),
                result.targetGender(),
                result.height(),
                result.weight(),
                result.tags(),
                result.createdAt(),
                result.updatedAt());
    }
}
