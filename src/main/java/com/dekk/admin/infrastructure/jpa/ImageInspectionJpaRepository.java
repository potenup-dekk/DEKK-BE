package com.dekk.admin.infrastructure.jpa;

import com.dekk.admin.domain.model.ImageInspection;
import com.dekk.admin.domain.model.InspectionStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageInspectionJpaRepository extends JpaRepository<ImageInspection, Long> {
    Optional<ImageInspection> findByCardImageId(Long cardImageId);

    Page<ImageInspection> findByStatus(InspectionStatus status, Pageable pageable);
}
