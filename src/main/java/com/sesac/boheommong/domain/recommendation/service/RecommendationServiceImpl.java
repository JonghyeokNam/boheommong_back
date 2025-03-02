package com.sesac.boheommong.domain.recommendation.service;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.recommendation.dto.*;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final ScoreCalculator scoreCalculator;
    private final InsuranceProductRepository productRepo;

    @Override
    public RecommendationResponseDto recommend(UserHealth userHealth) {
        // 1) ScoreCalculator => 점수
        var calcResult = scoreCalculator.calculate(userHealth);
        Map<InsuranceType, Integer> scores = calcResult.scores();

        // 2) DB 상품
        List<InsuranceProduct> allProducts = productRepo.findAll();

        // 3) 전역 인트로
        String globalIntro = buildGlobalIntro(userHealth);

        // 4) 점수 높은 순으로 정렬 => 상위 N개 (예: 5개)
        List<InsuranceType> sortedTypes = new ArrayList<>(scores.keySet());
        sortedTypes.sort((a, b) -> scores.get(b) - scores.get(a));

        int topN = Math.min(5, sortedTypes.size());
        List<RecommendationReasonGroupDto> categories = new ArrayList<>();

        for (int i = 0; i < topN; i++) {
            InsuranceType type = sortedTypes.get(i);

            // 권장 문구
            String reasonText = mapKorInsuranceName(type) + " 가입을 권장드립니다.";

            // 해당 카테고리 상품 => 최대 6개
            List<RecommendedProductDto> productDtos = findProductsByCategory(allProducts, type);
            if (productDtos.size() > 6) {
                productDtos = productDtos.subList(0, 6);
            }

            categories.add(new RecommendationReasonGroupDto(reasonText, productDtos));
        }

        return new RecommendationResponseDto(globalIntro, categories);
    }

    private String buildGlobalIntro(UserHealth userHealth) {
        // (A) 사용자명
        String userName = "사용자님";
        if (userHealth.getUser() != null && userHealth.getUser().getName() != null) {
            userName = userHealth.getUser().getName() + "님";
        }

        // (B) 위험요소 추출
        Set<String> riskFactors = gatherRiskFactors(userHealth);

        if (riskFactors.isEmpty()) {
            return userName + ", 고객님의 현재 상황을 종합적으로 검토한 결과,\n"
                    + "특별한 위험 요소는 확인되지 않았습니다.\n";
        }

        // (C) 문구 조립
        StringBuilder sb = new StringBuilder();
        sb.append(userName)
                .append(", 고객님의 현재 상황을 종합적으로 검토한 결과,\n")
                .append("아래와 같은 주요 위험 요소가 확인되었습니다:\n\n");

        for (String factor : riskFactors) {
            sb.append(factor).append("\n");
        }

        sb.append("\n위 요인들로 인해 향후 예기치 못한 부담이 발생할 수 있으므로,\n")
                .append("보험 가입을 통해 대비하시는 것을 권장드립니다.");

        return sb.toString();
    }

    private Set<String> gatherRiskFactors(UserHealth uh) {
        Set<String> factors = new LinkedHashSet<>();

        // 나이
        Integer age = uh.getAge();
        if (age != null && age >= 50) {
            factors.add("나이가 50세 이상");
        } else if (age != null && age >= 30) {
            factors.add("나이가 30세 이상");
        }

        // 흡연
        if (Boolean.TRUE.equals(uh.getIsSmoker())) {
            factors.add("흡연");
        }
        // 음주
        if (Boolean.TRUE.equals(uh.getIsDrinker())) {
            factors.add("음주습관");
        }
        // 가족력
        if (Boolean.TRUE.equals(uh.getHasFamilyHistory())) {
            factors.add("가족력이 있음");
        }
        // 자녀
        if (Boolean.TRUE.equals(uh.getHasChildren())) {
            factors.add("자녀가 있음");
        }
        // 자가주택
        if (Boolean.TRUE.equals(uh.getHasOwnHouse())) {
            factors.add("자가주택 보유");
        }
        // 반려동물
        if (Boolean.TRUE.equals(uh.getHasPet())) {
            factors.add("반려동물 양육");

        }
        // 직업
        if (uh.getJobType() != null) {
            switch (uh.getJobType()) {
                case OFFICE -> factors.add("직업: 사무직");
                case SELF_EMPLOYED -> factors.add("직업: 자영업");
                case DELIVERY -> factors.add("직업: 배달/라이더");
                case CONSTRUCTION -> factors.add("직업: 건설업");
                case HOUSEWIFE -> factors.add("직업: 주부");
                case STUDENT -> factors.add("직업: 학생");
                case UNEMPLOYED -> factors.add("직업: 무직");
            }
        }

        // 만성질환 (chronicDiseaseList)
        String diseaseList = uh.getChronicDiseaseList();
        if (diseaseList != null && !diseaseList.isBlank()) {
            factors.add("만성질환 보유");
        }

        // 혈압레벨
        if (uh.getBloodPressureLevel() != null && uh.getBloodPressureLevel() >= 4) {
            factors.add("고혈압 레벨");
        }
        // 혈당레벨
        if (uh.getBloodSugarLevel() != null && uh.getBloodSugarLevel() >= 4) {
            factors.add("고혈당 레벨");
        }

        return factors;
    }

    private List<RecommendedProductDto> findProductsByCategory(List<InsuranceProduct> all, InsuranceType type) {
        List<RecommendedProductDto> list = new ArrayList<>();
        for (InsuranceProduct p : all) {
            if (p.getProductCategory() == type) {
                list.add(new RecommendedProductDto(
                        p.getProductId(),
                        p.getCompanyName(),
                        p.getProductName(),
                        p.getMonthlyPremium(),
                        p.getCoverageDetails()
                ));
            }
        }
        return list;
    }

    private String mapKorInsuranceName(InsuranceType type) {
        return switch (type) {
            case CANCER -> "암보험";
            case LIFE -> "종신보험";
            case DENTAL -> "치과보험";
            case DEMENTIA -> "치매보험";
            case NURSING -> "간병보험";
            case DRIVER -> "운전자보험";
            case SURGERY -> "수술보험";
            case TRAVEL -> "여행자보험";
            case NEWBORN -> "어린이보험";
            case HEALTHCARE -> "실손보험";
            case FIRE -> "화재보험";
            case PET -> "펫보험";
            case CHILD -> "어린이보험";
            case ETC -> "기타보험";
        };
    }
}
