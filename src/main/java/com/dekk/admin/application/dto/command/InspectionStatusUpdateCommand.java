package com.dekk.admin.application.dto.command;

import com.dekk.admin.domain.model.InspectionStatus;

public record InspectionStatusUpdateCommand(Long inspectionId, InspectionStatus status, Long adminId) {}
