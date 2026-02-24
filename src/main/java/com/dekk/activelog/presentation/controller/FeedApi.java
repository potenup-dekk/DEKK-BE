package com.dekk.activelog.presentation.controller;

import com.dekk.activelog.presentation.response.UnseenCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Feed API", description = "메인 피드 및 카드 추천 관련 API")
public interface FeedApi {

    @Operation(summary = "미평가 카드 추출 API", description = "유저가 아직 스와이프(평가)하지 않은 새로운 카드를 최상단부터 size만큼 조회합니다.")
    ResponseEntity<List<UnseenCardResponse>> getUnseenCards(
        @Parameter(description = "임시 유저 ID (추후 토큰으로 대체)", example = "1")
        Long userId,

        @Parameter(description = "한 번에 가져올 카드 개수 (1~30)", example = "10")
        int size
    );
}
