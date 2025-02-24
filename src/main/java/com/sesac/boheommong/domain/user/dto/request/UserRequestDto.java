package com.sesac.boheommong.domain.user.dto.request;

public record UserRequestDto(
        String name,
        String loginEmail,
        String userEmail
) {
}
