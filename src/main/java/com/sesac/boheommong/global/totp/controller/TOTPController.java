package com.sesac.boheommong.global.totp.controller;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.jwt.domain.RefreshToken;
import com.sesac.boheommong.global.jwt.repository.RefreshTokenRepository;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.oauth2.util.CookieUtil;
import com.sesac.boheommong.global.totp.service.TOTPService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class TOTPController {

    private final UserService userService;
    private final TOTPService totpService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    @GetMapping("/qr")
    public ResponseEntity<?> getQrUrl(HttpServletRequest request) {
        String qrUrl = (String) request.getSession().getAttribute("QR_URL");
        if (qrUrl == null) {
            return ResponseEntity.badRequest().body("No QR available");
        }
        return ResponseEntity.ok(Map.of("qrUrl", qrUrl));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, String> body
    ) {
        Long userId = (Long) request.getSession().getAttribute("TOTP_PENDING_USERID");
        if (userId == null) {
            return ResponseEntity.status(401).body("No TOTP session");
        }
        User user = userService.getUserByUserIdOrElseThrow(userId);

        String codeStr = body.get("otpCode");
        int code = Integer.parseInt(codeStr);

        // ----------------------
        // 1) 최종등록이 이미 되어 있는지?
        // ----------------------
        if (user.getTotpSecret() != null) {
            // (A) 이미 최종 등록된 유저 => 'totpSecret'으로 매번 검증
            boolean valid = totpService.verifyCode(user.getTotpSecret(), code);
            if (!valid) {
                return ResponseEntity.status(401).body("Invalid TOTP code (final).");
            }

            // 검증 성공 => JWT 발급 etc...
            return doIssueTokensAndReturn(user, request, response);
        }

        // ----------------------
        // 2) 아직 최종등록 전 => pendingTotpSecret으로 검증
        // ----------------------
        String pendingSecret = user.getPendingTotpSecret();
        if (pendingSecret == null) {
            // 만약 여기도 null이면, 로직상 문제 or 이미 인증됨
            return ResponseEntity.badRequest().body("No pending secret. Maybe already verified?");
        }

        boolean valid = totpService.verifyCode(pendingSecret, code);
        if (!valid) {
            return ResponseEntity.status(401).body("Invalid TOTP code (pending).");
        }

        // => 검증 성공 => pending -> totpSecret 확정
        user.confirmTotpSecret();  // user.setTotpSecret(pending), pending=null
        userRepository.save(user);

        // 이후 토큰 발급 & 세션 정리
        return doIssueTokensAndReturn(user, request, response);
    }

    private ResponseEntity<?> doIssueTokensAndReturn(
            User user, HttpServletRequest request, HttpServletResponse response
    ) {
        // 1) Refresh 토큰 발급 & 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getUserId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 2) Access 토큰 발급 & 헤더에 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        response.setHeader("Authorization", "Bearer " + accessToken);

        // 3) 세션 정리
        request.getSession().removeAttribute("TOTP_PENDING_USERID");
        request.getSession().removeAttribute("QR_URL");

        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }


    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(RefreshToken.create(userId, newRefreshToken));
        refreshTokenRepository.save(refreshToken);
    }

    // 쿠키에 리프레시 토큰 등록
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정 값 제거
//    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//    }
}
