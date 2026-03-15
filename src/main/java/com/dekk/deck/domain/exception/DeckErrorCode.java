package com.dekk.deck.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeckErrorCode implements ErrorCode {
    USER_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "ED40001", "유저 ID는 필수값입니다"),
    DECK_NAME_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "ED40002", "보관함 이름은 필수값입니다"),
    DECK_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ED40003", "보관함 ID는 필수값입니다"),
    CARD_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ED40004", "카드 ID는 필수값입니다"),
    CUSTOM_DECK_NAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "ED40005", "커스텀 보관함 이름은 1자 이상 15자 이내여야 합니다"),
    CUSTOM_DECK_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40006", "커스텀 보관함은 최대 8개까지 생성할 수 있습니다"),
    DEFAULT_DECK_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "ED40007", "기본 보관함은 수정하거나 삭제할 수 없습니다"),
    CUSTOM_DECK_CARD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40008", "하나의 커스텀 보관함에는 최대 50개의 카드만 저장할 수 있습니다"),
    DECK_ROLE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ED40009", "보관함 권한 역할은 필수값입니다"),
    DECK_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40010", "보관함은 최대 9개까지 참여 및 생성할 수 있습니다"),
    DECK_GUEST_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40011", "해당 보관함의 참여 인원이 가득 찼습니다 (최대 5명)"),
    HOST_CANNOT_LEAVE_DECK(HttpStatus.BAD_REQUEST, "ED40012", "호스트는 쉐어덱을 자진 퇴장할 수 없습니다. 공유 끄기를 이용해 주세요"),

    SHARE_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ED40101", "유효하지 않거나 만료된 초대 링크입니다"),

    DECK_IS_NOT_SHARED(HttpStatus.FORBIDDEN, "ED40301", "현재 공유 중인 보관함이 아닙니다"),
    GUEST_CANNOT_GENERATE_TOKEN(HttpStatus.FORBIDDEN, "ED40302", "초대 링크는 보관함의 호스트만 발급할 수 있습니다"),

    DEFAULT_DECK_NOT_FOUND(HttpStatus.NOT_FOUND, "ED40401", "기본 보관함을 찾을 수 없습니다"),
    CARD_NOT_FOUND_IN_DECK(HttpStatus.NOT_FOUND, "ED40402", "보관함에 해당 카드가 존재하지 않습니다"),
    CUSTOM_DECK_NOT_FOUND(HttpStatus.NOT_FOUND, "ED40403", "커스텀 보관함을 찾을 수 없습니다"),
    DECK_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ED40404", "보관함의 참여자 정보를 찾을 수 없습니다"),

    ALREADY_JOINED_DECK(HttpStatus.CONFLICT, "ED40901", "이미 참여 중인 보관함입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return httpStatus;
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
