package com.dekk.admin.presentation.controller;

import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.admin.domain.model.InspectionStatus;
import com.dekk.admin.presentation.request.InspectionStatusUpdateRequest;
import com.dekk.admin.presentation.response.ImageInspectionResponse;
import com.dekk.admin.security.AdminUserDetails;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "이미지 검수 관리 API", description = "관리자 백오피스용 이미지 검수 관리 API입니다.")
public interface AdminInspectionApi {

    @Operation(summary = "상태별 이미지 검수 목록 조회 (기본값: AI_PASSED, 복수 상태 가능)")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<PageResponse<ImageInspectionResponse>>> getInspections(
            @Parameter(description = "조회할 상태값 (복수 가능, 기본값: AI_PASSED)") @RequestParam(defaultValue = "AI_PASSED")
                    List<InspectionStatus> status,
            Pageable pageable);

    @Operation(summary = "검수 상태 변경 (승인/반려)")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> updateInspectionStatus(
            @Parameter(description = "검수 ID") @PathVariable Long inspectionId,
            @Valid @RequestBody InspectionStatusUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);
}
