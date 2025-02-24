package com.sesac.boheommong.domain.userhealth.entity;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.userhealth.enums.JobType;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * [UserHealth 엔티티]
 *
 * - 사용자(User)와 1:1 관계
 * - '건강 정보' 및 '생활/직업 정보'를 통합 관리
 * - 예: 나이, 키, 몸무게, 흡연/음주 여부, 혈압/혈당(5단계), 만성질환, 수술 횟수(0~5), 직업, 자녀유무, 자가주택유무, 반려동물유무, 가족력유무 등
 *
 * 가정:
 *  - 몸무게, 키는 모두 정수(cm, kg)
 *  - 만성질환(10종: 고혈압, 당뇨, 고지혈증, 천식, 관절염, 뇌졸중, 협심증, 암, 간염, 심부전) -> 콤마 구분
 *  - 직업(JobType): 7개 (사무직, 배달, 건설, 자영업, 학생, 주부, 무직)
 *  - 혈압(bloodPressureLevel) / 혈당(bloodSugarLevel): (1~5단계) 라디오/셀렉트
 *  - 수술 횟수(surgeryCount): 0~5
 *  - 흡연/음주: Boolean (yes/no)
 *  - 자녀(hasChildren) / 자가주택(hasOwnHouse) / 반려동물(hasPet) / 가족력(hasFamilyHistory): Boolean
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user_health SET is_deleted = true, deleted_at = now() WHERE health_id = ?")
@SQLRestriction("is_deleted = FALSE")
@Table(name = "user_health")
public class UserHealth extends BaseEntity {

    /**
     * PK: 건강정보 ID (auto_increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long healthId;

    /**
     * [1:1 연관관계]
     *  - user_health 테이블에 user_id (FK) 존재
     *  - 한 User가 하나의 UserHealth 가질 수 있음
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    /**
     * [기본 건강/신체 정보]
     *  - 나이(age): 정수 (예: 30)
     *  - 성별(gender): "M"/"F" 등
     *  - 키(height): 정수(cm) (예: 170)
     *  - 몸무게(weight): 정수(kg) (예: 65)
     *  - BMI: 자동계산 가능 -> (weight / ((height/100) * (height/100)))
     *    - null 가능 (height나 weight가 0이거나 미입력일 때)
     */
    private Integer age;
    private String  gender;
    private Integer height;
    private Integer weight;
    private Float   bmi;

    /**
     * [혈압 / 혈당: 5단계]
     *  - bloodPressureLevel (1~5)
     *    ex: 1=저혈압, 2=정상, 3=경계, 4=고혈압1, 5=고혈압2
     *  - bloodSugarLevel (1~5)
     *    ex: 1=저혈당, 2=정상, 3=공복혈당장애, 4=당뇨전단계, 5=당뇨
     *
     * UI: 라디오 or 셀렉트
     */
    private Integer bloodPressureLevel; // 1~5
    private Integer bloodSugarLevel;    // 1~5

    /**
     * [수술 횟수]
     *  - 0~5 중 하나 선택 (셀렉트)
     */
    private Integer surgeryCount;  // 0..5

    /**
     * [흡연/음주 여부]
     *  - Boolean: true=Yes, false=No
     *  - UI: 체크박스 or 라디오
     */
    private Boolean isSmoker;
    private Boolean isDrinker;

    /**
     * [만성질환 목록]
     *  - 10종 예시: "고혈압,당뇨,고지혈증,천식,관절염,뇌졸중,협심증,암,간염,심부전"
     *  - 콤마로 구분해서 저장 (ex: "고혈압,당뇨")
     */
    @Column(length = 255)
    private String chronicDiseaseList;

    /**
     * [직업]
     *  - 7가지 JobType Enum (사무직, 배달, 건설, 자영업, 학생, 주부, 무직)
     *  - UI: 셀렉트 or 라디오
     */
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    /**
     * [가정/생활 관련]
     *  - 자녀 유무(hasChildren)
     *  - 자가주택 여부(hasOwnHouse)
     *  - 반려동물 여부(hasPet)
     *  - 가족력 여부(hasFamilyHistory) -> 암, 치매 등
     *  - 모두 Boolean(체크박스/라디오)
     */
    private Boolean hasChildren;
    private Boolean hasOwnHouse;
    private Boolean hasPet;
    private Boolean hasFamilyHistory;

    /**
     * [정적 팩토리 메서드] create(...)
     *  - 새로운 UserHealth 객체 생성
     *  - BMI는 height, weight 값 유효 시 자동 계산
     */
    public static UserHealth create(
            User user,
            Integer age,
            String gender,
            Integer height,
            Integer weight,
            Integer bloodPressureLevel,
            Integer bloodSugarLevel,
            Integer surgeryCount,
            Boolean isSmoker,
            Boolean isDrinker,
            String chronicDiseaseList,
            JobType jobType,
            Boolean hasChildren,
            Boolean hasOwnHouse,
            Boolean hasPet,
            Boolean hasFamilyHistory
    ) {
        UserHealth uh = new UserHealth();

        // 1) 연관 User
        uh.user = user;

        // 2) 기초(나이,성별,키,몸무게)
        uh.age = age;
        uh.gender = gender;
        uh.height = height;
        uh.weight = weight;

        // 3) BMI 계산
        if (height != null && height > 0 && weight != null && weight > 0) {
            float hMeter = height / 100f;
            uh.bmi = weight / (hMeter * hMeter);
        } else {
            uh.bmi = null;
        }

        // 4) 혈압/혈당
        uh.bloodPressureLevel = bloodPressureLevel;
        uh.bloodSugarLevel = bloodSugarLevel;

        // 5) 수술 횟수
        uh.surgeryCount = surgeryCount;

        // 6) 흡연/음주
        uh.isSmoker = isSmoker;
        uh.isDrinker = isDrinker;

        // 7) 만성질환
        uh.chronicDiseaseList = chronicDiseaseList;

        // 8) 직업
        uh.jobType = jobType;

        // 9) 생활정보
        uh.hasChildren = hasChildren;
        uh.hasOwnHouse = hasOwnHouse;
        uh.hasPet = hasPet;
        uh.hasFamilyHistory = hasFamilyHistory;

        return uh;
    }

    /**
     * [updateHealth(...) 메서드]
     *  - 기존 UserHealth 객체의 값을 갱신
     *  - BMI 재계산 로직 동일
     */
    public void updateHealth(
            Integer age,
            String gender,
            Integer height,
            Integer weight,
            Integer bloodPressureLevel,
            Integer bloodSugarLevel,
            Integer surgeryCount,
            Boolean isSmoker,
            Boolean isDrinker,
            String chronicDiseaseList,
            JobType jobType,
            Boolean hasChildren,
            Boolean hasOwnHouse,
            Boolean hasPet,
            Boolean hasFamilyHistory
    ) {
        // 1) 기초
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;

        // BMI 재계산
        if (height != null && height > 0 && weight != null && weight > 0) {
            float hMeter = height / 100f;
            this.bmi = weight / (hMeter * hMeter);
        } else {
            this.bmi = null;
        }

        // 2) 혈압/혈당
        this.bloodPressureLevel = bloodPressureLevel;
        this.bloodSugarLevel = bloodSugarLevel;

        // 3) 수술 횟수
        this.surgeryCount = surgeryCount;

        // 4) 흡연/음주
        this.isSmoker = isSmoker;
        this.isDrinker = isDrinker;

        // 5) 만성질환
        this.chronicDiseaseList = chronicDiseaseList;

        // 6) 직업
        this.jobType = jobType;

        // 7) 생활정보
        this.hasChildren = hasChildren;
        this.hasOwnHouse = hasOwnHouse;
        this.hasPet = hasPet;
        this.hasFamilyHistory = hasFamilyHistory;
    }
}
