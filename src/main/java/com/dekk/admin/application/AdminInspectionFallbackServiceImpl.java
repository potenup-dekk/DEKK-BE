package com.dekk.admin.application;

import com.dekk.admin.domain.model.InspectionStatus;
import com.dekk.admin.infrastructure.jpa.ImageInspectionJpaRepository;
import com.dekk.common.worker.InspectionFallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminInspectionFallbackServiceImpl implements InspectionFallbackHandler {

    private final ImageInspectionJpaRepository inspectionRepository;

    @Override
    @Transactional
    public void handleFailure(Long cardImageId) {
        log.error("[Admin Fallback] n8n 워커 통신 실패. Admin 도메인에서 예외 처리 시작 - cardImageId: {}", cardImageId);

        inspectionRepository
                .findByCardImageId(cardImageId)
                .ifPresentOrElse(
                        inspection -> {
                            inspection.updateAiResult(InspectionStatus.WORKER_ERROR, "AI 서버 통신 실패로 검수 지연");
                            log.info("[Admin Fallback] 검수 상태 WORKER_ERROR로 업데이트 완료 - cardImageId: {}", cardImageId);
                        },
                        () -> log.warn("[Admin Fallback] 검수 정보를 찾을 수 없습니다. 상태 업데이트 실패 - cardImageId: {}", cardImageId));
    }
}
