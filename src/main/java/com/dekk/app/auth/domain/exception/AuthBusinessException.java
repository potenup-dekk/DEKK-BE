package com.dekk.app.auth.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class AuthBusinessException extends BusinessException {
    public AuthBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
