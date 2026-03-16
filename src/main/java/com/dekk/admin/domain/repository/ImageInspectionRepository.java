package com.dekk.admin.domain.repository;

import com.dekk.admin.domain.model.ImageInspection;
import com.dekk.admin.domain.model.InspectionStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImageInspectionRepository {
    ImageInspection getById(Long id);

    ImageInspection getByCardImageId(Long cardImageId);

    Page<ImageInspection> getByStatuses(List<InspectionStatus> statuses, Pageable pageable);

    ImageInspection save(ImageInspection imageInspection);
}
