package com.dekk.app.crawl.application.result;

import com.dekk.app.crawl.domain.model.CrawlRawData;

public record CrawlRawDataResult(Long rawDataId, String status) {
    public static CrawlRawDataResult from(CrawlRawData rawData) {
        return new CrawlRawDataResult(rawData.getId(), rawData.getStatus().name());
    }
}
