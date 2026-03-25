package com.dekk.app.card.application.command;

public record ProductImageCreateCommand(String originUrl, String imageUrl, boolean isUploaded) {}
