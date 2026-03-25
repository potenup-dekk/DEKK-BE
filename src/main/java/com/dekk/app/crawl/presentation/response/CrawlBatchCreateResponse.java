package com.dekk.app.crawl.presentation.response;

import com.dekk.app.card.domain.model.enums.Platform;
import com.dekk.app.crawl.application.result.CrawlBatchResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record CrawlBatchCreateResponse(
        @Schema(description = "배치 ID", example = "1") Long batchId,
        @Schema(description = "플랫폼", example = "MUSINSA") Platform platform,

        @Schema(description = "배치 상태", example = "COLLECTING")
        String status) {
    public static CrawlBatchCreateResponse from(CrawlBatchResult result) {
        return new CrawlBatchCreateResponse(result.batchId(), result.platform(), result.status());
    }
}
