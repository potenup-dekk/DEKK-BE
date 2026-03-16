package com.dekk.card.application.dto.command;

public record RequestDeleteCardCommand(Long cardId, Long adminId, String reason) {}
