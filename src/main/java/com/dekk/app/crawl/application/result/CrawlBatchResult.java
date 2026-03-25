package com.dekk.app.crawl.application.result;

import com.dekk.app.card.domain.model.enums.Platform;
import com.dekk.app.crawl.domain.model.CrawlBatch;

public record CrawlBatchResult(Long batchId, Platform platform, String status) {
    public static CrawlBatchResult from(CrawlBatch batch) {
        return new CrawlBatchResult(
                batch.getId(), batch.getPlatform(), batch.getStatus().name());
    }
}
