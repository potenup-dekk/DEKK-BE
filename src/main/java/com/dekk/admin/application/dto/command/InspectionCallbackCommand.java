package com.dekk.admin.application.dto.command;

import com.dekk.admin.domain.model.InspectionStatus;

public record InspectionCallbackCommand(Long cardImageId, InspectionStatus status, String aiComment) {}
