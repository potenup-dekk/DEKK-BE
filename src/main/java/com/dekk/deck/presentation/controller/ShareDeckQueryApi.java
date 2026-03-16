package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.presentation.response.GuestSharedDeckCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "쉐어덱 조회 API", description = "쉐어덱 프리뷰 및 조회 API")
public interface ShareDeckQueryApi {

    @Operation(
            summary = "초대 링크 기반 쉐어덱 프리뷰 조회 (비회원 접근 허용)",
            description =
                    "초대 링크의 토큰을 사용하여 쉐어덱의 카드 목록을 조회합니다. 비회원 접근을 고려하여 가격, 브랜드, 구매 링크 등의 상품 정보는 응답에서 완전히 차단되며, 썸네일과 신체 정보, 태그만 제공됩니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20017)")
    @ApiErrorExceptions({DeckErrorCode.class})
    ResponseEntity<ApiResponse<List<GuestSharedDeckCardResponse>>> getSharedDeckPreview(
            @Parameter(description = "쉐어덱 초대 토큰", in = ParameterIn.PATH) String token);
}
