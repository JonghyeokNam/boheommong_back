package com.sesac.boheommong.domain.recommendation.service;

import com.sesac.boheommong.domain.recommendation.dto.RecommendationResponseDto;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;

import java.util.List;

public interface RecommendationService {
    /**
     * ScoreCalculator로 점수와 이유 목록을 얻은 뒤,
     * 이유들을 자연스럽게 합쳐 문장 생성 + 상품 매핑
     */
    RecommendationResponseDto recommend(UserHealth userHealth);
}
