package com.sesac.boheommong.domain.userhealth.service;

import com.sesac.boheommong.domain.userhealth.dto.request.UserHealthRequestDto;
import com.sesac.boheommong.domain.userhealth.dto.response.UserHealthResponseDto;

public interface UserHealthService {

    /**
     * 건강정보 등록 (1:1 관계에서 없을 때만 가능)
     * @param userId   : 로그인된 사용자의 PK
     * @param request  : 입력 DTO
     * @return 등록된 결과(ResponseDto)
     */
    UserHealthResponseDto createHealth(Long userId, UserHealthRequestDto request);

    /**
     * 건강정보 수정 (이미 존재해야 함)
     * @param userId   : 로그인된 사용자
     * @param request  : 수정 DTO
     * @return 수정된 결과(ResponseDto)
     */
    UserHealthResponseDto updateHealth(Long userId, UserHealthRequestDto request);

    /**
     * 건강정보 조회
     * @param userId 사용자 PK
     * @return 찾은 결과(ResponseDto) or 예외
     */
    UserHealthResponseDto getMyHealth(Long userId);

    void deleteHealth(Long userId);
}
