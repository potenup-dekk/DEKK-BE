package com.dekk.app.admin.domain.repository;

import com.dekk.app.admin.domain.model.ImageInspection;
import com.dekk.app.admin.domain.model.InspectionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImageInspectionRepository {
    ImageInspection getById(Long id);

    ImageInspection getByCardImageId(Long cardImageId);

    Optional<ImageInspection> findByCardImageId(Long cardImageId);

    Page<ImageInspection> getByStatuses(List<InspectionStatus> statuses, Pageable pageable);

    ImageInspection save(ImageInspection imageInspection);
}
