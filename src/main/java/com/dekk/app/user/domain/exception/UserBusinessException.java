package com.dekk.app.user.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class UserBusinessException extends BusinessException {
    public UserBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
