package com.sesac.boheommong.global.totp.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public String createQrUrlFromSecret(String secret, String userEmail) {
        // 필요한 파라미터
        String issuer = "Mong";      // 원하는 발급자명
        String algorithm = "SHA1";   // 기본: SHA1
        int digits = 6;             // 6자리
        int period = 30;            // 30초 주기

        try {
            // URL 인코딩
            String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8);
            String encodedEmail  = URLEncoder.encode(userEmail, StandardCharsets.UTF_8);
            String encodedSecret = URLEncoder.encode(secret, StandardCharsets.UTF_8);

            // 최종 otpauth:// URL
            // 예) otpauth://totp/Mong:user@site.com?secret=ABCDE123...&issuer=Mong&algorithm=SHA1&digits=6&period=30
            String encodedpth =  "otpauth://totp/"
                    + encodedIssuer + ":" + encodedEmail
                    + "?secret=" + encodedSecret
                    + "&issuer=" + encodedIssuer
                    + "&algorithm=" + algorithm
                    + "&digits=" + digits
                    + "&period=" + period;

            String encoded = URLEncoder.encode(encodedpth, StandardCharsets.UTF_8);
            return "https://api.qrserver.com/v1/create-qr-code/?data="
                    + encoded
                    + "&size=200x200";  // 원하는 크기

        } catch (Exception e) {
            throw new RuntimeException("Failed to build QR URL", e);
        }
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
