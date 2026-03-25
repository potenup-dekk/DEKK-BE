package com.dekk.global.worker;

public interface InspectionWorkerClient {
    void sendInspectionRequest(Long cardImageId, String originUrl, String imageUrl);
}
