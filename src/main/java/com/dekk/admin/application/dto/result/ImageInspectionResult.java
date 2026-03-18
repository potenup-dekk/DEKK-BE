package com.dekk.admin.application.dto.result;

import com.dekk.admin.domain.model.ImageInspection;
import com.dekk.admin.domain.model.InspectionStatus;
import java.time.LocalDateTime;

public record ImageInspectionResult(
        Long id,
        Long cardImageId,
        String imageUrl,
        InspectionStatus status,
        String aiComment,
        Long adminId,
        LocalDateTime createdAt) {
    public static ImageInspectionResult from(ImageInspection entity) {
        return new ImageInspectionResult(
                entity.getId(),
                entity.getCardImageId(),
                entity.getImageUrl(),
                entity.getStatus(),
                entity.getAiComment(),
                entity.getAdminId(),
                entity.getCreatedAt());
    }
}
