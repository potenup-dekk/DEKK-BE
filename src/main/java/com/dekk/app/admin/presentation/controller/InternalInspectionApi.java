package com.dekk.app.admin.presentation.controller;

import com.dekk.app.admin.domain.exception.AdminErrorCode;
import com.dekk.app.admin.presentation.request.InspectionCallbackRequest;
import com.dekk.global.response.ApiResponse;
import com.dekk.global.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "내부 시스템 통신 콜백 API", description = "내부 시스템(n8n) 통신 검수 콜백하는 API")
public interface InternalInspectionApi {

    @Operation(summary = "AI 검수 결과 콜백 수신")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> handleCallback(@Valid @RequestBody InspectionCallbackRequest request);
}
