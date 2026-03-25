package com.dekk.app.admin.presentation.request;

import com.dekk.app.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.dekk.app.admin.domain.model.InspectionStatus;
import jakarta.validation.constraints.NotNull;

public record InspectionStatusUpdateRequest(
        @NotNull(message = "변경할 상태값은 필수입니다.") InspectionStatus status) {
    public InspectionStatusUpdateCommand toCommand(Long inspectionId, Long adminId) {
        return new InspectionStatusUpdateCommand(inspectionId, status, adminId);
    }
}
