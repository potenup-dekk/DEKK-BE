package com.dekk.user.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.presentation.request.UserOnboardingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Command API", description = "사용자 정보 및 상태 변경 API")
public interface UserCommandApi {

    @Operation(summary = "유저 온보딩", description = "소셜 로그인 후 최초 신체 정보 및 닉네임을 설정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SU20001)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청(EG001) / 이미 온보딩 됨(EU40001) / 닉네임 규칙 위반(EU40003) / 신체 정보 범위 오류(EU40006)",
                    content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음(EU40401)",
                    content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 사용 중인 닉네임(EU40901)",
                    content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
            )
    })
    ResponseEntity<ApiResponse<Void>> onboardUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserOnboardingRequest request
    );
}
