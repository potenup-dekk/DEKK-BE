package com.dekk.app.activelog.application.dto.command;

import com.dekk.app.activelog.domain.model.SwipeType;

public record SwipeCommand(Long userId, Long cardId, SwipeType swipeType) {}
