package com.sesac.boheommong.domain.user.service;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.dto.request.UserRequestDto;
import com.sesac.boheommong.domain.user.dto.response.UserResponseDto;

public interface UserService {
    /**
     * userId로 사용자 조회. 없으면 예외 발생
     */
    User getUserByUserIdOrElseThrow(Long userId);

    /**
     * loginEmail로 사용자 조회. 없으면 예외 발생
     */
    User getUserByLoginEmailOrElseThrow(String loginEmail);

    /**
     * 사용자 정보 업데이트
     * @param loginEmail : 수정하려는 사용자(로그인 이메일 식별)
     * @param userInfoRequestDto : 새로 갱신할 정보
     * @return 수정된 사용자 정보를 UserResponseDto로 반환
     */
    UserResponseDto updateUser(String loginEmail, UserRequestDto userInfoRequestDto);

    /**
     * 신규 유저인지 여부 반환
     * (ex: 건강정보나 특정 필드가 있는지에 따라 true/false)
     */
    Boolean getCheckNewUser(String loginEmail);
}
