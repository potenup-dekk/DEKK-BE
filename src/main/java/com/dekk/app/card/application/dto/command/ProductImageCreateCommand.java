package com.dekk.app.card.application.dto.command;

public record ProductImageCreateCommand(String originUrl, String imageUrl, boolean isUploaded) {}
