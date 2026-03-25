package com.dekk.app.crawl.infrastructure.worker;

public interface InspectionWorkerClient {
    void sendInspectionRequest(Long cardImageId, String originUrl, String imageUrl);
}
