package com.sesac.boheommong.domain.user.dto.response;

import com.sesac.boheommong.domain.user.entity.User;

/**
 * 사용자(User) 기본 정보만 내려줄 때 사용할 응답 DTO
 */
public record UserResponseDto(
        Long userId,
        String name,
        String loginEmail,
        String userEmail
) {
    /**
     * User 엔티티를 UserResponseDto로 변환
     */
    public static UserResponseDto toDto(User user) {
        if (user == null) {
            return null; // 또는 예외 처리를 하셔도 됩니다.
        }

        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getLoginEmail(),
                user.getUserEmail()
        );
    }
}
