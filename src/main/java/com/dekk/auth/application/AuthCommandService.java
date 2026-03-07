package com.dekk.auth.application;

import com.dekk.auth.application.command.TokenRefreshCommand;
import com.dekk.auth.application.dto.result.TokenRefreshResult;
import com.dekk.auth.domain.exception.AuthBusinessException;
import com.dekk.auth.domain.exception.AuthErrorCode;
import com.dekk.auth.domain.model.BlacklistAccessToken;
import com.dekk.auth.domain.model.RefreshToken;
import com.dekk.auth.domain.repository.BlacklistRepository;
import com.dekk.auth.domain.repository.RefreshTokenRepository;
import com.dekk.auth.jwt.JwtTokenProvider;
import com.dekk.security.oauth2.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;

    public TokenRefreshResult refreshToken(TokenRefreshCommand command) {
        jwtTokenProvider.validateToken(command.refreshToken());

        if(!jwtTokenProvider.isRefreshToken(command.refreshToken())) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(command.refreshToken());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        RefreshToken storedToken = refreshTokenRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new AuthBusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        if (!storedToken.getToken().equals(command.refreshToken())) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenRepository.save(RefreshToken.create(userDetails.getId(), newRefreshToken));

        return new TokenRefreshResult(newAccessToken, newRefreshToken);
    }

    public void logout(Long userId, String accessToken) {
        refreshTokenRepository.deleteByUserId(userId);

        long remainingExpirationTime = jwtTokenProvider.getRemainingExpirationTime(accessToken);

        if (remainingExpirationTime > 0) {
            BlacklistAccessToken blacklistAccessToken = BlacklistAccessToken.create(accessToken);
            blacklistRepository.save(blacklistAccessToken, remainingExpirationTime);
        }
    }
}
