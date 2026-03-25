package com.dekk.app.admin.presentation.request;

import com.dekk.app.admin.application.dto.command.InspectionCallbackCommand;
import com.dekk.app.admin.domain.model.InspectionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InspectionCallbackRequest(
        @NotNull(message = "카드 이미지 ID는 필수입니다.") Long imageId,

        @NotNull(message = "상태값은 필수입니다.") InspectionStatus status,

        @Size(max = 500, message = "코멘트는 500자를 초과할 수 없습니다.") String comment) {
    public InspectionCallbackCommand toCommand() {
        return new InspectionCallbackCommand(imageId, status, comment);
    }
}
