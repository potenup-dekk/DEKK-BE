package com.dekk.admin.presentation.controller;

import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.admin.presentation.request.AdminLoginRequest;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 인증 API", description = "관리자 인증 관련 API입니다.")
public interface AdminAuthApi {

    @Operation(summary = "관리자 로그인", description = "이메일과 비밀번호를 사용하여 관리자 액세스 토큰을 발급받습니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관리자 로그인 성공")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> login(AdminLoginRequest request, HttpServletResponse response);

    @Operation(summary = "관리자 로그아웃", description = "관리자 로그아웃 처리를 수행합니다. (클라이언트 단 토큰 삭제 필요)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관리자 로그아웃 성공")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response);
}
