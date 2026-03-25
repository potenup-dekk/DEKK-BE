package com.dekk.app.crawl.presentation.response;

import com.dekk.app.crawl.application.result.CrawlRawDataResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record CrawlRawDataCreateResponse(
        @Schema(description = "원본 데이터 ID", example = "1") Long rawDataId,
        @Schema(description = "처리 상태", example = "PENDING") String status) {
    public static CrawlRawDataCreateResponse from(CrawlRawDataResult result) {
        return new CrawlRawDataCreateResponse(result.rawDataId(), result.status());
    }
}
