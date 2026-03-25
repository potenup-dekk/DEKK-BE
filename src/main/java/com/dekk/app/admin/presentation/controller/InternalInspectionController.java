package com.dekk.app.admin.presentation.controller;

import com.dekk.app.admin.application.AdminInspectionCommandService;
import com.dekk.app.admin.presentation.request.InspectionCallbackRequest;
import com.dekk.app.admin.presentation.response.AdminResultCode;
import com.dekk.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/i/v1/inspections")
@RequiredArgsConstructor
public class InternalInspectionController implements InternalInspectionApi {

    private final AdminInspectionCommandService commandService;

    @Override
    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<Void>> handleCallback(@Valid @RequestBody InspectionCallbackRequest request) {
        commandService.processInspectionCallback(request.toCommand());
        return ResponseEntity.ok(ApiResponse.of(AdminResultCode.INSPECTION_CALLBACK_SUCCESS, null));
    }
}
