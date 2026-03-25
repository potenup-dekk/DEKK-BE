package com.dekk.app.admin.application;

import com.dekk.app.admin.application.dto.command.InspectionCallbackCommand;
import com.dekk.app.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.dekk.app.admin.domain.model.ImageInspection;
import com.dekk.app.admin.domain.repository.ImageInspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInspectionCommandService {

    private final ImageInspectionRepository imageInspectionRepository;

    @Transactional
    public void processInspectionCallback(InspectionCallbackCommand command) {
        ImageInspection inspection = imageInspectionRepository.getByCardImageId(command.cardImageId());
        inspection.updateAiResult(command.status(), command.aiComment());
    }

    @Transactional
    public void updateInspectionStatus(InspectionStatusUpdateCommand command) {
        ImageInspection inspection = imageInspectionRepository.getById(command.inspectionId());
        inspection.updateAdminStatus(command.status(), command.adminId());
    }
}
