package com.dekk.app.crawl.domain.exception;

import com.dekk.global.error.BusinessException;
import com.dekk.global.error.ErrorCode;

public class CrawlBusinessException extends BusinessException {

    public CrawlBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
