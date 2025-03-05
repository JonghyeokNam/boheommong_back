package com.sesac.boheommong.global.oauth2.handler;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.totp.service.TOTPService; // TOTP Service
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final TOTPService totpService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        // 1) DB에서 User 조회
        User user = userService.getUserByLoginEmailOrElseThrow(email);

        // 2) totpSecret, pendingTotpSecret 판단
        if (user.getTotpSecret() == null) {
            // totpSecret 아직 없음 => (A) *진짜* 최초 등록 OR (B) 등록 미완료 상태
            if (user.getPendingTotpSecret() == null) {
                // (A) 정말로 처음 => QR 생성
                TOTPService.TotpInitResult result = totpService.createSecretAndQR(user.getLoginEmail());

                // DB: pending 에만 저장
                user.setPendingTotpSecret(result.getSecret());
                userRepository.save(user);

                // 세션에 QR_URL, userId 저장
                request.getSession().setAttribute("QR_URL", result.getOtpAuthUrl());
                request.getSession().setAttribute("TOTP_PENDING_USERID", user.getUserId());

                log.info("[OAuth2SuccessHandler] First TOTP: created pending secret. userId={}", user.getUserId());
            } else {

                // 1) 기존 pending secret 가져오기
                String oldPendingSecret = user.getPendingTotpSecret();

                // 2) 해당 secret으로 다시 QR URL 생성 (새 secret 발급X)
                //    -> TOTPService에 createQrUrlFromSecret(...) 메서드 작성
                String reusedQrUrl = totpService.createQrUrlFromSecret(oldPendingSecret, user.getLoginEmail());


                request.getSession().setAttribute("QR_URL", reusedQrUrl);
                request.getSession().setAttribute("TOTP_PENDING_USERID", user.getUserId());


                log.info("[OAuth2SuccessHandler] Already pending. Re-generate QR for userId={}", user.getUserId());
            }

            // => 무조건 QR 페이지로 이동
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/otp/qr");
        }
        else {
            // 3) 이미 totpSecret != null => 매번 OTP
            request.getSession().setAttribute("TOTP_PENDING_USERID", user.getUserId());
            log.info("[OAuth2SuccessHandler] totpSecret exists => OTP stage. userId={}", user.getUserId());

            // OTP 입력 페이지로
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/otp");
        }
    }

}
