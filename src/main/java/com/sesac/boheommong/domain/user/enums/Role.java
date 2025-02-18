package com.sesac.boheommong.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin"),
    PARTNER("partner"),
    USER("user");

    private final String role;
}
