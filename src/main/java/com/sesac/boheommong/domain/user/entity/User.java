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
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = now() where user_id = ?")
@SQLRestriction("is_deleted is FALSE")
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;       // PK

    @NotNull
    private String loginEmail; // 로그인 계정(카카오에서 가져온 이메일 or 별도)

    @NotNull
    private String userEmail;  // 사용자 이메일(일반 이메일, 추가 용도)

    @NotNull
    private String name;       // 사용자 이름(닉네임)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "totp_enabled")
    private boolean totpEnabled = true; // 기본 true

    @Column(name = "totp_secret")
    private String totpSecret; // null이면 아직 TOTP 등록 전

    // private 생성자
    private User(String name, String loginEmail, String userEmail, Role role) {
        this.name = name;
        this.loginEmail = loginEmail;
        this.userEmail = userEmail;
        this.role = role;
    }

    // 정적 팩토리 메서드
    public static User create(String name, String loginEmail, String userEmail, Role role) {
        User user = new User(name, loginEmail, userEmail, role);
        user.totpEnabled = true; // 명시적으로 설정
        return user;
    }

    // 필요한 update 메서드가 있다면 추가
    public void updateInfo(String newEmail) {
        this.userEmail = newEmail;
    }

    public void setTotpSecret(String secret) {
        this.totpSecret = secret;
    }

    public void disableTotp() {
        this.totpEnabled = false;
        this.totpSecret = null;
    }
}
