package com.dekk.app.crawl.infrastructure.worker.dto;

public record CardImageInspectionPayload(Long cardImageId, String originUrl, String imageUrl) {}
