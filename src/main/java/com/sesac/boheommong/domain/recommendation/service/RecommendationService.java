package com.sesac.boheommong.domain.recommendation.service;

import com.sesac.boheommong.domain.recommendation.dto.RecommendationResponseDto;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;

import java.util.List;

public interface RecommendationService {
    RecommendationResponseDto recommend(UserHealth userHealth);
}
