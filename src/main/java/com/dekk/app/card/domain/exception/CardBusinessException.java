package com.dekk.app.card.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class CardBusinessException extends BusinessException {
    public CardBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
