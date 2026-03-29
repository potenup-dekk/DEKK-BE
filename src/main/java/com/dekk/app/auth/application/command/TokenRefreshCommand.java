package com.dekk.app.auth.application.command;

import com.dekk.app.auth.domain.exception.AuthBusinessException;
import com.dekk.app.auth.domain.exception.AuthErrorCode;
import org.springframework.util.StringUtils;

public record TokenRefreshCommand(String refreshToken) {
    public TokenRefreshCommand {
        if (!StringUtils.hasText(refreshToken)) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
