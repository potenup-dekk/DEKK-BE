package com.dekk.app.admin.presentation.response;

import com.dekk.global.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AdminResultCode implements ResultCode {
    ADMIN_LOGIN_SUCCESS(HttpStatus.OK, "SAD20001", "관리자 로그인에 성공했습니다."),
    ADMIN_LOGOUT_SUCCESS(HttpStatus.OK, "SAD20002", "관리자 로그아웃에 성공했습니다."),
    INSPECTION_STATUS_UPDATED(HttpStatus.OK, "SAD20003", "이미지 검수 상태가 성공적으로 변경되었습니다."),
    INSPECTION_LIST_QUERIED(HttpStatus.OK, "SAD20004", "이미지 검수 목록 조회에 성공했습니다."),
    INSPECTION_CALLBACK_SUCCESS(HttpStatus.OK, "SAD20005", "AI 검수 결과가 성공적으로 반영되었습니다.");

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
