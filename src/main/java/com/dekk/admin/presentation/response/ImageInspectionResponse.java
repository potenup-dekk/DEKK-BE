package com.dekk.admin.presentation.response;

import com.dekk.admin.application.dto.result.ImageInspectionResult;
import com.dekk.admin.domain.model.InspectionStatus;
import java.time.LocalDateTime;

public record ImageInspectionResponse(
        Long id,
        Long cardImageId,
        String imageUrl,
        InspectionStatus status,
        String aiComment,
        Long adminId,
        LocalDateTime createdAt) {
    public static ImageInspectionResponse from(ImageInspectionResult result) {
        return new ImageInspectionResponse(
                result.id(),
                result.cardImageId(),
                result.imageUrl(),
                result.status(),
                result.aiComment(),
                result.adminId(),
                result.createdAt());
    }
}
