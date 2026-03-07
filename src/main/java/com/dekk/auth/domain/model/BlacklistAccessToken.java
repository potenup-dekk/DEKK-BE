package com.dekk.auth.domain.model;

import com.dekk.auth.domain.exception.AuthBusinessException;
import com.dekk.auth.domain.exception.AuthErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BlacklistAccessToken {

    private String accessToken;
    private String status;

    private BlacklistAccessToken(String accessToken, String status) {
        this.accessToken = accessToken;
        this.status = status;
    }

    public static BlacklistAccessToken create(String accessToken) {
        if(accessToken == null || accessToken.isBlank()) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_TOKEN);
        }
        return new BlacklistAccessToken(accessToken, "logout");
    }

}
