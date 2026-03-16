package com.dekk.card.recommend.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RecommendResultCode implements ResultCode {
    RECOMMEND_CARD_SUCCESS(HttpStatus.OK, "RC200001", "추천 카드 조회가 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
