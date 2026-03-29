package com.dekk.app.admin.application.dto.command;

import com.dekk.app.admin.domain.model.InspectionStatus;

public record InspectionCallbackCommand(Long cardImageId, InspectionStatus status, String aiComment) {}
