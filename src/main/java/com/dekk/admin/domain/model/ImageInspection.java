package com.dekk.admin.domain.model;

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

@Entity
@Table(name = "image_inspections", indexes = {
    @Index(name = "idx_image_inspections_product_image", columnList = "productImageId"),
    @Index(name = "idx_image_inspections_status", columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageInspection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productImageId;

    @Column(nullable = false, columnDefinition = "text")
    private String imageUrl;

    @Column(nullable = false, length = 255)
    private String productTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InspectionStatus status = InspectionStatus.PENDING;

    @Column(name = "ai_comment", length = 500)
    private String aiComment;

    private ImageInspection(Long productImageId, String imageUrl, String productTitle) {
        this.productImageId = productImageId;
        this.imageUrl = imageUrl;
        this.productTitle = productTitle;
    }

    public static ImageInspection create(Long productImageId, String imageUrl, String productTitle) {
        return new ImageInspection(productImageId, imageUrl, productTitle);
    }

    public void updateAiResult(InspectionStatus status, String aiComment) {
        this.status = status;
        this.aiComment = aiComment;
    }
}
