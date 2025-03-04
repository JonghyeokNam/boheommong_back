package com.sesac.boheommong.global.totp.controller;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.jwt.domain.RefreshToken;
import com.sesac.boheommong.global.jwt.repository.RefreshTokenRepository;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.oauth2.util.CookieUtil;
import com.sesac.boheommong.global.totp.service.TOTPService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class TOTPController {

    private final UserService userService;
    private final TOTPService totpService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    @GetMapping("/qr")
    public ResponseEntity<?> getQrUrl(HttpServletRequest request) {
        String qrUrl = (String) request.getSession().getAttribute("QR_URL");
        System.out.println(qrUrl);
        log.info("Session ID: " + request.getSession().getId());
        log.info("QR_URL: " + request.getSession().getAttribute("QR_URL"));
        if (qrUrl == null) {
            System.out.println("두번째에러러러러럴");
            return ResponseEntity.badRequest().body("No QR available");
        }
        return ResponseEntity.ok(Map.of("qrUrl", qrUrl));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody Map<String, String> body) {
        Long userId = (Long) request.getSession().getAttribute("TOTP_PENDING_USERID");
        if (userId == null) {
            return ResponseEntity.status(401).body("No TOTP session");
        }
        User user = userService.getUserByUserIdOrElseThrow(userId);

        String codeStr = body.get("otpCode");
        int code = Integer.parseInt(codeStr);


        boolean valid = totpService.verifyCode(user.getTotpSecret(), code);
        if (!valid) {
            return ResponseEntity.status(401).body("Invalid TOTP code");
        }

        // ========== 이제 최종 JWT 발급 ==========
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getUserId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        response.setHeader("Authorization", "Bearer " + accessToken);

        // 세션 정리
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
