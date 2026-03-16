package com.dekk.deck.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SharedDeckJoinRequest(
        @Schema(description = "초대 링크에 포함된 발급 토큰", example = "a1b2c3d4e5f64789b123c456d789e012")
        @NotBlank(message = "초대 토큰은 필수입니다.")
        String token) {}
