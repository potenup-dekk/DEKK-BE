package com.dekk.app.activelog.presentation.request;

import com.dekk.app.activelog.domain.model.SwipeType;
import jakarta.validation.constraints.NotNull;

public record SwipeRequest(
        @NotNull(message = "스와이프 타입(LIKE/DISLIKE)은 필수입니다.") SwipeType swipeType) {}
