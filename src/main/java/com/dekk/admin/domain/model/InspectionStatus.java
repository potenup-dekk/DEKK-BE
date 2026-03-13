package com.dekk.admin.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InspectionStatus {
    PENDING("검수 대기"),
    AI_PASSED("AI 자동 통과"),
    AI_FLAGGED("위험군 (관리자 확인 필요)"),
    WORKER_ERROR("AI 워커 통신 실패"),
    ADMIN_APPROVED("관리자 최종 승인"),
    ADMIN_REJECTED("관리자 최종 반려");

    private final String description;
}
