package com.dekk.app.admin.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class AdminBusinessException extends BusinessException {

    private final ErrorCode errorCode;

    public AdminBusinessException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
