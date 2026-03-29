package com.dekk.app.auth.application.dto.result;

public record TokenRefreshResult(String accessToken, String refreshToken) {}
