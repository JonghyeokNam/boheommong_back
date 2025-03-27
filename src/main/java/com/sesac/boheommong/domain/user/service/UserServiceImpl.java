package com.sesac.boheommong.domain.user.service;

import com.sesac.boheommong.domain.user.dto.request.UserRequestDto;
import com.sesac.boheommong.domain.user.dto.response.UserResponseDto;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.userhealth.repository.UserHealthRepository;
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
    private final UserHealthRepository userHealthRepository;


    @Override
    public UserResponseDto updateUser(String loginEmail, UserRequestDto userRequestDto) {
        // 유저 조회
        User user = getUserByLoginEmailOrElseThrow(loginEmail);

        String userEmail = userRequestDto.userEmail();

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
        // 1) 유저 조회
        User user = getUserByLoginEmailOrElseThrow(loginEmail);

        // 2) userHealth 조회
        //  - row가 없으면 => 신규
        //  - row가 있고 age == null이면 => 신규
        //  - 그 외 => 기존 유저
        return userHealthRepository.findByUser(user)
                .map(userHealth -> {
                    // age가 null이면 아직 입력 안 한 상태 => 신규
                    return (userHealth.getAge() == null);
                })
                .orElse(true);
        // orElse(true) => userHealth row 자체가 없으면 => 신규
    }
}
