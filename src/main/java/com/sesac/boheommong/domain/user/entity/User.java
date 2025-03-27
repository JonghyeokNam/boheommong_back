package com.sesac.boheommong.domain.user.entity;

import com.sesac.boheommong.domain.user.enums.Role;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = now() WHERE user_id = ?")
@SQLRestriction("is_deleted is FALSE")
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // PK

    @NotNull
    private String loginEmail; // 로그인 계정(카카오에서 가져온 이메일 or 별도)

    @NotNull
    private String userEmail;  // 사용자 이메일(일반 이메일, 추가 용도)

    @NotNull
    private String name;       // 사용자 이름(닉네임)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 실제 TOTP 시크릿 (Google Auth에 완전히 등록된 상태)
     * null이면 아직 최종 등록 안 된 상태
     */
    @Column(name = "totp_secret")
    private String totpSecret;

    /**
     * QR 생성 후 아직 인증(검증) 전인 임시 시크릿
     * 사용자가 QR을 스캔/OTP를 검증하면 -> totpSecret에 옮기고 이 값은 null로 되돌린다
     */
    @Column(name = "pending_totp_secret")
    private String pendingTotpSecret;

    /**
     * 기본적으로 2FA(TOTP) 사용 플래그
     * ex) 상황에 따라 false로 비활성화할 수도 있음
     */
    @Column(name = "totp_enabled")
    private boolean totpEnabled = true;

    // ---- 생성자 / 팩토리 메서드 ----
    private User(String name, String loginEmail, String userEmail, Role role) {
        this.name = name;
        this.loginEmail = loginEmail;
        this.userEmail = userEmail;
        this.role = role;
    }

    public static User create(String name, String loginEmail, String userEmail, Role role) {
        User user = new User(name, loginEmail, userEmail, role);
        user.totpEnabled = true; // 기본값
        return user;
    }

    public void updateInfo(String newEmail) {
        this.userEmail = newEmail;
    }

    // ---- 비즈니스 로직 ----
    public void setPendingTotpSecret(String secret) {
        this.pendingTotpSecret = secret;
    }

    public void confirmTotpSecret() {
        // pending 에 있던 값을 최종 totpSecret 으로 적용
        this.totpSecret = this.pendingTotpSecret;
        this.pendingTotpSecret = null;
    }

    public void disableTotp() {
        this.totpEnabled = false;
        this.totpSecret = null;
        this.pendingTotpSecret = null;
    }
}
