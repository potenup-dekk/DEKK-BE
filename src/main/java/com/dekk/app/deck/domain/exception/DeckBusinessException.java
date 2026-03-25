package com.dekk.app.deck.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class DeckBusinessException extends BusinessException {
    public DeckBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
