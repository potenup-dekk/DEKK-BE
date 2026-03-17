package com.dekk.admin.infrastructure;

import com.dekk.admin.domain.exception.AdminBusinessException;
import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.admin.domain.model.ImageInspection;
import com.dekk.admin.domain.model.InspectionStatus;
import com.dekk.admin.domain.repository.ImageInspectionRepository;
import com.dekk.admin.infrastructure.jpa.ImageInspectionJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageInspectionRepositoryImpl implements ImageInspectionRepository {

    private final ImageInspectionJpaRepository imageInspectionJpaRepository;

    @Override
    public ImageInspection getById(Long id) {
        return imageInspectionJpaRepository
                .findById(id)
                .orElseThrow(() -> new AdminBusinessException(AdminErrorCode.INSPECTION_NOT_FOUND));
    }

    @Override
    public ImageInspection getByCardImageId(Long cardImageId) {
        return imageInspectionJpaRepository
                .findByCardImageId(cardImageId)
                .orElseThrow(() -> new AdminBusinessException(AdminErrorCode.INSPECTION_NOT_FOUND));
    }

    @Override
    public Optional<ImageInspection> findByCardImageId(Long cardImageId) {
        return imageInspectionJpaRepository.findByCardImageId(cardImageId);
    }

    @Override
    public Page<ImageInspection> getByStatuses(List<InspectionStatus> statuses, Pageable pageable) {
        return imageInspectionJpaRepository.findByStatusIn(statuses, pageable);
    }

    @Override
    public ImageInspection save(ImageInspection imageInspection) {
        return imageInspectionJpaRepository.save(imageInspection);
    }
}
