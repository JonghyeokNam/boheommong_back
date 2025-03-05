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
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        // 1) DB에서 User 조회
        User user = userService.getUserByLoginEmailOrElseThrow(email);

        // 2) totpSecret이 없으면 => 최초 TOTP 등록 (QR)
        if (user.getTotpSecret() == null) {
            // 새 시크릿 & QR URL 생성
            TOTPService.TotpInitResult result = totpService.createSecretAndQR(user.getLoginEmail());

            // DB 저장
            user.setTotpSecret(result.getSecret());
            userRepository.save(user);

            // 세션에 QR_URL, userId 저장
            request.getSession().setAttribute("QR_URL", result.getOtpAuthUrl());
            request.getSession().setAttribute("TOTP_PENDING_USERID", user.getUserId());

            // QR 페이지로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/otp/qr");
        }
        // 3) 시크릿이 있으면 => 매번 OTP
        else {
            request.getSession().setAttribute("TOTP_PENDING_USERID", user.getUserId());
            // OTP 입력 페이지로
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/otp");
        }
    }
}