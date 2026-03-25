package com.dekk.app.crawl.infrastructure.worker;

public interface InspectionFallbackHandler {
    void handleFailure(Long targetId);
}
