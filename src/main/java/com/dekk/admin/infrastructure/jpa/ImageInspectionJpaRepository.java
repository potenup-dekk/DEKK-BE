package com.dekk.admin.infrastructure.jpa;

import com.dekk.admin.domain.model.ImageInspection;
import com.dekk.admin.domain.model.InspectionStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageInspectionJpaRepository extends JpaRepository<ImageInspection, Long> {
    Optional<ImageInspection> findByProductImageId(Long productImageId);

    // 위험군(AI_FLAGGED) 등 상태 기반 조회를 위한 페이징 메서드
    Page<ImageInspection> findByStatus(InspectionStatus status, Pageable pageable);
}
