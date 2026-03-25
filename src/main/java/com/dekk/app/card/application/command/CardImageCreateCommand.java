package com.dekk.app.card.application.command;

public record CardImageCreateCommand(String originUrl, String imageUrl, boolean isUploaded) {}
