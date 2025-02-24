package com.sesac.boheommong.domain.user.service;

import com.sesac.boheommong.domain.user.dto.request.UserInfoRequestDto;
import com.sesac.boheommong.domain.user.dto.response.UserResponseDto;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto updateUser(String loginEmail, UserInfoRequestDto userInfoRequestDto) {
        // 유저 조회
        User user = getUserByLoginEmailOrElseThrow(loginEmail);

        String userEmail = userInfoRequestDto.userEmail();

        // 유저 정보 업데이트 (email 등)
        user.updateInfo(
                userEmail
        );

        User saveUser = userRepository.save(user);
        return UserResponseDto.toDto(saveUser);
    }

    @Override
    public User getUserByUserIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserByLoginEmailOrElseThrow(String loginEmail) {
        return userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));
    }

    // 신규 유저 구분
    @Override
    public Boolean getCheckNewUser(String loginEmail) {
        User user = getUserByLoginEmailOrElseThrow(loginEmail);
        return (user.getUserEmail() == null || user.getUserEmail().isEmpty());
    }
}
