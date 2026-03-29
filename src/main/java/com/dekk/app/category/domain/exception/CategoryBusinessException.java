package com.dekk.app.category.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class CategoryBusinessException extends BusinessException {
    public CategoryBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
