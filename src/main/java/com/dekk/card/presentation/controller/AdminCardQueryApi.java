package com.dekk.card.presentation.controller;

import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.presentation.dto.response.AdminCardResponse;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "관리자 카드 목록 조회 API", description = "관리자가 카드 목록을 조회하는 API")
public interface AdminCardQueryApi {

    @Operation(
            summary = "관리자 카드 목록 조회",
            description = "관리자가 카드 목록을 페이징하여 조회합니다. 상태, 카드 ID, 원본 ID, 기간으로 필터링할 수 있으며 정렬 기준을 지정할 수 있습니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관리자 카드 목록 조회 성공")
    ResponseEntity<ApiResponse<PageResponse<AdminCardResponse>>> getAdminCards(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "생성일 정렬 방향 (ASC: 오래된순, DESC: 최신순)", example = "DESC")
                    @RequestParam(defaultValue = "DESC")
                    String sort,
            @Parameter(description = "카드 상태 필터") @RequestParam(required = false) CardStatus status,
            @Parameter(description = "카드 ID 검색") @RequestParam(required = false) Long cardId,
            @Parameter(description = "원본 ID 검색") @RequestParam(required = false) String originId,
            @Parameter(description = "조회 시작일 (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false)
                    LocalDate startDate,
            @Parameter(description = "조회 종료일 (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false)
                    LocalDate endDate);
}
