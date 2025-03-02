package com.sesac.boheommong.domain.recommendation.controller;

import com.sesac.boheommong.domain.recommendation.dto.RecommendationResponseDto;
import com.sesac.boheommong.domain.recommendation.service.RecommendationService;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import com.sesac.boheommong.domain.userhealth.repository.UserHealthRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserHealthRepository userHealthRepository;

    @Operation(summary = "보험상품 추천", description = "ScoreCalculator + 자연어 치환 + 상품 매핑을 통해 추천 결과를 반환")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="추천 성공"),
            @ApiResponse(responseCode="404", description="사용자 혹은 건강정보가 없을 경우 발생")
    })
    @GetMapping
    public Response<RecommendationResponseDto> getRecommendations(HttpServletRequest request) {
        // 1) JWT에서 로그인 이메일
        String loginEmail = tokenProvider.getUserLoginEmail(request);

        // 2) user 조회
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);

        // 3) userHealth 조회
        UserHealth userHealth = userHealthRepository.findByUser(user)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_HEALTH_NOT_FOUND));

        // 4) 추천 로직
        RecommendationResponseDto result = recommendationService.recommend(userHealth);

        return Response.success(result);
    }
}
