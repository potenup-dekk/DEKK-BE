package com.dekk.admin.domain.model;

import com.dekk.admin.domain.exception.AdminBusinessException;
import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
        name = "image_inspections",
        indexes = {
            @Index(name = "idx_image_inspections_card_image", columnList = "cardImageId"),
            @Index(name = "idx_image_inspections_status", columnList = "status")
        })
@SQLDelete(sql = "UPDATE image_inspections SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageInspection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cardImageId;

    @Column(nullable = false, columnDefinition = "text")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InspectionStatus status = InspectionStatus.PENDING;

    @Column(name = "ai_comment", length = 500)
    private String aiComment;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(nullable = false)
    private boolean deleted = false;

    private ImageInspection(Long cardImageId, String imageUrl) {
        this.cardImageId = cardImageId;
        this.imageUrl = imageUrl;
    }

    public static ImageInspection create(Long cardImageId, String imageUrl) {
        return new ImageInspection(cardImageId, imageUrl);
    }

    public void updateAiResult(InspectionStatus targetStatus, String aiComment) {
        if (this.status == targetStatus) {
            return;
        }
        if (!this.status.isProcessableByAi()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_INSPECTION_STATUS_TRANSITION);
        }
        this.status = targetStatus;
        this.aiComment = aiComment;
    }

    public void updateAdminStatus(InspectionStatus targetStatus, Long adminId) {
        if (this.status == targetStatus) {
            return;
        }
        if (!this.status.isProcessableByAdmin()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_INSPECTION_STATUS_TRANSITION);
        }
        if (!this.status.canTransitionToAdminStatus(targetStatus)) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_TARGET_INSPECTION_STATUS);
        }
        this.status = targetStatus;
        this.adminId = adminId;
    }
}
