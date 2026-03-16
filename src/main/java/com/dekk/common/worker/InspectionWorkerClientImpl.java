package com.dekk.common.worker;

import com.dekk.common.worker.dto.CardImageInspectionPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionWorkerClientImpl implements InspectionWorkerClient {

    private final RestTemplate restTemplate;
    private final InspectionFallbackHandler fallbackHandler;

    @Value("${worker.n8n.webhook-url}")
    private String webhookUrl;

    @Async("workerTaskExecutor")
    @Override
    public void sendInspectionRequest(Long cardImageId, String originUrl, String imageUrl) {
        log.info("[Inspection Worker] n8n 검수 요청 시작 - cardImageId: {}", cardImageId);
        CardImageInspectionPayload payload = new CardImageInspectionPayload(cardImageId, originUrl, imageUrl);

        try {
            restTemplate.postForEntity(webhookUrl, payload, Void.class);
            log.info("[Inspection Worker] n8n 검수 요청 성공 - cardImageId: {}", cardImageId);
        } catch (Exception e) {
            log.error("[Inspection Worker] n8n 연동 실패 - cardImageId: {}, Reason: {}", cardImageId, e.getMessage(), e);

            fallbackHandler.handleFailure(cardImageId);
        }
    }
}
