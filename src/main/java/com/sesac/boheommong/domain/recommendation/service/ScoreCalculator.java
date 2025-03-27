package com.sesac.boheommong.domain.recommendation.service;

import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import com.sesac.boheommong.domain.userhealth.enums.JobType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ScoreCalculator {

    public CategoryScoreResult calculate(UserHealth uh) {
        // 1) 카테고리별 기본점수 = 10
        Map<InsuranceType, Integer> scores = new HashMap<>();
        for (InsuranceType t : InsuranceType.values()) {
            scores.put(t, 10);
        }

        // 2) 조건별로 점수 누적
        // (A) 나이
        Integer age = uh.getAge();
        if (age != null) {
            if (age >= 50) {
                addScore(scores, InsuranceType.CANCER, 10);
                addScore(scores, InsuranceType.LIFE, 7);
                addScore(scores, InsuranceType.DEMENTIA, 5);
                addScore(scores, InsuranceType.NURSING, 5);
            } else if (age >= 30) {
                addScore(scores, InsuranceType.CANCER, 7);
                addScore(scores, InsuranceType.LIFE, 5);
            } else if (age >= 10){
                // 30 미만
                addScore(scores, InsuranceType.TRAVEL, 3);
            } else {
                addScore(scores, InsuranceType.CHILD, 3);
            }
        }

        // (B) 흡연
        if (Boolean.TRUE.equals(uh.getIsSmoker())) {
            addScore(scores, InsuranceType.CANCER, 8);
            addScore(scores, InsuranceType.SURGERY, 4);
            addScore(scores, InsuranceType.LIFE, 5);
        }

        // (C) 음주
        if (Boolean.TRUE.equals(uh.getIsDrinker())) {
            addScore(scores, InsuranceType.SURGERY, 5);
            addScore(scores, InsuranceType.DRIVER, 5);
            addScore(scores, InsuranceType.DEMENTIA, 5);
        }

        // (D) 만성질환
        String diseaseList = uh.getChronicDiseaseList();
        if (diseaseList != null && !diseaseList.isBlank()) {
            String[] arr = diseaseList.split(",");
            for (String item : arr) {
                String disease = item.trim().toUpperCase();
                switch(disease) {
                    case "HYPERTENSION" -> {
                        addScore(scores, InsuranceType.LIFE, 8);
                        addScore(scores, InsuranceType.HEALTHCARE, 5);
                    }
                    case "DIABETES" -> {
                        addScore(scores, InsuranceType.CANCER, 10);
                        addScore(scores, InsuranceType.SURGERY, 5);
                        addScore(scores, InsuranceType.LIFE, 5);
                        addScore(scores, InsuranceType.HEALTHCARE, 10);
                    }
                    case "CANCER" -> {
                        addScore(scores, InsuranceType.CANCER, 20);
                        addScore(scores, InsuranceType.LIFE, 12);
                    }
                    case "STROKE" -> {
                        addScore(scores, InsuranceType.DEMENTIA, 8);
                        addScore(scores, InsuranceType.NURSING, 10);
                    }
                    case "HYPERLIPIDEMIA" -> {
                        addScore(scores, InsuranceType.LIFE, 5);
                        addScore(scores, InsuranceType.HEALTHCARE, 5);
                    }
                    case "ASTHMA" -> {
                        addScore(scores, InsuranceType.SURGERY, 5);
                        addScore(scores, InsuranceType.HEALTHCARE, 5);
                    }
                    case "ARTHRITIS" -> {
                        addScore(scores, InsuranceType.SURGERY, 8);
                    }
                    case "ANGINA" -> {
                        addScore(scores, InsuranceType.LIFE, 10);
                        addScore(scores, InsuranceType.HEALTHCARE, 8);
                    }
                    case "HEPATITIS" -> {
                        addScore(scores, InsuranceType.HEALTHCARE, 8);
                    }
                    case "HEARTFAILURE" -> {
                        addScore(scores, InsuranceType.LIFE, 10);
                        addScore(scores, InsuranceType.HEALTHCARE, 8);
                    }
                    case "MIGRAINE" -> {
                        addScore(scores, InsuranceType.HEALTHCARE, 8);
                    }
                    case "OSTEOPOROSIS" -> {
                        addScore(scores, InsuranceType.SURGERY, 8);
                        addScore(scores, InsuranceType.HEALTHCARE, 5);
                    }
                    case "COPD" -> {
                        addScore(scores, InsuranceType.SURGERY, 5);
                        addScore(scores, InsuranceType.HEALTHCARE, 10);
                    }
                    case "LIVER_CIRRHOSIS" -> {
                        addScore(scores, InsuranceType.HEALTHCARE, 10);
                        addScore(scores, InsuranceType.LIFE, 10);
                    }
                    case "KIDNEY_DISEASE" -> {
                        addScore(scores, InsuranceType.LIFE, 10);
                        addScore(scores, InsuranceType.HEALTHCARE, 10);
                    }
                    case "THYROID_DISEASE" -> {
                        addScore(scores, InsuranceType.CANCER, 5);
                        addScore(scores, InsuranceType.HEALTHCARE, 5);
                    }
                }
            }
        }

        // (E) 가족력
        if (Boolean.TRUE.equals(uh.getHasFamilyHistory())) {
            addScore(scores, InsuranceType.CANCER, 5);
            addScore(scores, InsuranceType.LIFE, 5);
        }

        // (F) 직업
        if (uh.getJobType() != null) {
            JobType job = uh.getJobType();
            switch (job) {
                case OFFICE -> {
                    addScore(scores, InsuranceType.HEALTHCARE, 3);
                    addScore(scores, InsuranceType.TRAVEL, 3);
                }
                case SELF_EMPLOYED -> {
                    addScore(scores, InsuranceType.SURGERY, 5);
                }
                case DELIVERY -> {
                    addScore(scores, InsuranceType.DRIVER, 8);
                }
                case CONSTRUCTION -> {
                    addScore(scores, InsuranceType.SURGERY, 8);
                }
                case HOUSEWIFE -> {
                    addScore(scores, InsuranceType.CHILD, 5);
                    addScore(scores, InsuranceType.LIFE, 3);
                }
                case STUDENT -> {
                    addScore(scores, InsuranceType.TRAVEL, 8);
                }
                case UNEMPLOYED -> {
                    addScore(scores, InsuranceType.HEALTHCARE, 3);
                }
            }
        }

        // (G) 수술 횟수
        if (uh.getSurgeryCount() != null) {
            int sc = uh.getSurgeryCount();
            if (sc == 1 || sc == 2) {
                addScore(scores, InsuranceType.SURGERY, sc * 3); // 3 또는 6 점
            } else if (sc == 3 || sc == 4) {
                addScore(scores, InsuranceType.SURGERY, 8);
                addScore(scores, InsuranceType.LIFE, 5);
            } else if (sc >= 5) {
                addScore(scores, InsuranceType.SURGERY, 10);
                addScore(scores, InsuranceType.LIFE, 7);
                addScore(scores, InsuranceType.HEALTHCARE, 5);
            }
        }

        // (H) 자녀
        if (Boolean.TRUE.equals(uh.getHasChildren())) {
            addScore(scores, InsuranceType.CHILD, 7);
            addScore(scores, InsuranceType.LIFE, 5);
        }

        // (I) 자가주택
        if (Boolean.TRUE.equals(uh.getHasOwnHouse())) {
            addScore(scores, InsuranceType.FIRE, 5);
        }

        // (J) 반려동물
        if (Boolean.TRUE.equals(uh.getHasPet())) {
            addScore(scores, InsuranceType.PET, 7);
        }

        // (K) 혈압레벨
        if (uh.getBloodPressureLevel() != null && uh.getBloodPressureLevel() >= 4) {
            addScore(scores, InsuranceType.LIFE, 5);
            addScore(scores, InsuranceType.HEALTHCARE, 3);
        }

        // (L) 혈당레벨
        if (uh.getBloodSugarLevel() != null && uh.getBloodSugarLevel() >= 4) {
            addScore(scores, InsuranceType.CANCER, 3);
            addScore(scores, InsuranceType.HEALTHCARE, 5);
        }

        // 최종
        return new CategoryScoreResult(scores);
    }

    private void addScore(Map<InsuranceType,Integer> scores, InsuranceType type, int plus) {
        scores.put(type, scores.get(type) + plus);
    }

    public static record CategoryScoreResult(
            Map<InsuranceType,Integer> scores
    ) {}
}
