package com.dekk.card.presentation.request;

import com.dekk.card.application.dto.command.RequestDeleteCardCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestDeleteCardRequest(
        @NotBlank(message = "삭제 사유는 필수값입니다") @Size(max = 100, message = "삭제 사유는 100자 이하로 입력해주세요")
        String reason) {
    public RequestDeleteCardCommand toCommand(Long cardId, Long adminId) {
        return new RequestDeleteCardCommand(cardId, adminId, reason);
    }
}
