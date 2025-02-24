package com.sesac.boheommong.domain.userhealth.service;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.userhealth.dto.request.UserHealthRequest;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import com.sesac.boheommong.domain.userhealth.repository.UserHealthRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserHealthServiceImpl implements UserHealthService {

    private final UserHealthRepository userHealthRepository;
    private final UserRepository userRepository;

    /**
     * 건강정보 생성 (1:1 관계에서 이미 존재하면 예외)
     */
    @Transactional
    @Override
    public UserHealth createHealth(UserHealthRequest req) {
        // 1) userId로 User 찾기
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 이미 HealthInfo가 있는지 검사 (1:1 관계)
        userHealthRepository.findByUser(user)
                .ifPresent(info -> {
                    throw BaseException.from(ErrorCode.USER_HEALTH_ALREADY_EXISTS);
                });


        // 3) 엔티티 생성
        UserHealth info = UserHealth.create(
                user,
                req.userName(),
                req.age(),
                req.gender(),
                req.height(),
                req.weight(),
                req.bmi(),
                req.isSmoker(),
                req.isDrinker(),
                req.hasChronicDisease(),
                req.chronicDiseaseList(),
                req.surgeryHistory(),
                req.bloodPressure(),
                req.bloodSugar()
        );

        // 4) 저장
        return userHealthRepository.save(info);
    }

    /**
     * 건강정보 업데이트
     */
    @Transactional
    @Override
    public UserHealth updateHealth(UserHealthRequest req) {
        // 1) userId로 User 찾기
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 해당 user로 HealthInfo 조회
        UserHealth info = userHealthRepository.findByUser(user)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_HEALTH_NOT_FOUND));

        // 3) 업데이트
        info.updateHealth(
                req.userName(),
                req.age(),
                req.gender(),
                req.height(),
                req.weight(),
                req.bmi(),
                req.isSmoker(),
                req.isDrinker(),
                req.hasChronicDisease(),
                req.chronicDiseaseList(),
                req.surgeryHistory(),
                req.bloodPressure(),
                req.bloodSugar()
        );

        return info; // 필요 시 DTO로 변환
    }

    /**
     * 사용자 ID로 건강정보 조회
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<UserHealth> findHealthByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));
        return userHealthRepository.findByUser(user);
    }
}
