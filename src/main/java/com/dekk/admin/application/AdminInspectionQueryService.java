package com.dekk.admin.application;

import com.dekk.admin.application.dto.result.ImageInspectionResult;
import com.dekk.admin.domain.model.InspectionStatus;
import com.dekk.admin.domain.repository.ImageInspectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInspectionQueryService {

    private final ImageInspectionRepository imageInspectionRepository;

    public Page<ImageInspectionResult> getInspectionsByStatuses(List<InspectionStatus> statuses, Pageable pageable) {
        return imageInspectionRepository.getByStatuses(statuses, pageable).map(ImageInspectionResult::from);
    }
}
