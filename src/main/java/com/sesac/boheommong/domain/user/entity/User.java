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
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = now() where id = ?")
@SQLRestriction("is_deleted is FALSE")
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull private String oauthId;
    @NotNull private String email;
    @NotNull private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
