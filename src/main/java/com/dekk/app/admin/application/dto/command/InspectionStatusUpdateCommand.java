package com.dekk.app.admin.application.dto.command;

import com.dekk.app.admin.domain.model.InspectionStatus;

public record InspectionStatusUpdateCommand(Long inspectionId, InspectionStatus status, Long adminId) {}
