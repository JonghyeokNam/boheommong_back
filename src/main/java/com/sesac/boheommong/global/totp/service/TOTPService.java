package com.sesac.boheommong.global.totp.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class TOTPService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    /**
     * 시크릿 & QR 코드 URL 생성
     */
    public TotpInitResult createSecretAndQR(String userEmail) {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();

        // otpauth://totp/{issuer}:{userEmail}?secret={secret}&issuer={issuer}
        String issuer = "Mong";
        String otpAuthUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, userEmail, key);

        return new TotpInitResult(secret, otpAuthUrl);
    }

    /**
     * 사용자가 입력한 OTP 코드 검증
     */
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }

    @Data
    @AllArgsConstructor
    public static class TotpInitResult {
        private String secret;      // Base32 시크릿
        private String otpAuthUrl;  // QR 코드 스캔용 URL
    }
}
