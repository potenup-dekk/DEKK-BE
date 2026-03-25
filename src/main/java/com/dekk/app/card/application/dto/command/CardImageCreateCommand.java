package com.dekk.app.card.application.dto.command;

public record CardImageCreateCommand(String originUrl, String imageUrl, boolean isUploaded) {}
