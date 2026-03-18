package com.dekk.admin.presentation.request;

import com.dekk.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.dekk.admin.domain.model.InspectionStatus;
import jakarta.validation.constraints.NotNull;

public record InspectionStatusUpdateRequest(
        @NotNull(message = "변경할 상태값은 필수입니다.") InspectionStatus status) {
    public InspectionStatusUpdateCommand toCommand(Long inspectionId, Long adminId) {
        return new InspectionStatusUpdateCommand(inspectionId, status, adminId);
    }
}
