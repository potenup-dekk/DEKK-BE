package com.dekk.app.activelog.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class ActiveLogBusinessException extends BusinessException {
    public ActiveLogBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
