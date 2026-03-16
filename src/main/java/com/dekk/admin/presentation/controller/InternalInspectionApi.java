package com.dekk.admin.presentation.controller;

import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.admin.presentation.request.InspectionCallbackRequest;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "내부 시스템 통신 콜백 API", description = "내부 시스템(n8n) 통신 검수 콜백 API 입니다.")
public interface InternalInspectionApi {

    @Operation(summary = "AI 검수 결과 콜백 수신")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> handleCallback(@Valid @RequestBody InspectionCallbackRequest request);
}
