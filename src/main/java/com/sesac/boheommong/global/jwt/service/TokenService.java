package com.sesac.boheommong.global.jwt.service;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.jwt.domain.RefreshToken;
import com.sesac.boheommong.global.jwt.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 새로운 액세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        validateRefreshTokenOrElseThrow(refreshToken);

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.getUserByUserIdOrElseThrow(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String loginEmail) {
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        refreshTokenRepository.delete(refreshToken);
    }

    // 토큰 유효성 검사
    public void validateRefreshTokenOrElseThrow(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

}
