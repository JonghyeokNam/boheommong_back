package com.sesac.boheommong.domain.userhealth.entity;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_health")
public class UserHealth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long healthId;

    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user; // User 필드 (1:1 연관)

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "height")
    private Float height;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "bmi")
    private Float bmi;

    @Column(name = "is_smoker")
    private Integer isSmoker;  // 0 or 1

    @Column(name = "is_drinker")
    private Integer isDrinker; // 0 or 1

    @Column(name = "has_chronic_disease")
    private Integer hasChronicDisease; // 0 or 1

    @Column(name = "chronic_disease_list", length = 255)
    private String chronicDiseaseList;

    @Column(name = "surgery_history", length = 255)
    private String surgeryHistory;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "blood_sugar", length = 20)
    private String bloodSugar;

    // (A) --------------- 기본 생성자 (JPA용) --------------- //
    public UserHealth() {
        // JPA가 리플렉션으로 객체 생성 시 사용
        // 아무 로직도 없어도 됩니다.
    }

    // (B) --------------- private 생성자 --------------- //
    private UserHealth(
            User user,
            String userName,
            Integer age,
            String gender,
            Float height,
            Float weight,
            Float bmi,
            Integer isSmoker,
            Integer isDrinker,
            Integer hasChronicDisease,
            String chronicDiseaseList,
            String surgeryHistory,
            String bloodPressure,
            String bloodSugar
    ) {
        this.user = user;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.isSmoker = isSmoker;
        this.isDrinker = isDrinker;
        this.hasChronicDisease = hasChronicDisease;
        this.chronicDiseaseList = chronicDiseaseList;
        this.surgeryHistory = surgeryHistory;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
    }

    // (C) --------------- 정적 팩토리 메서드 --------------- //
    public static UserHealth create(
            User user,
            String userName,
            Integer age,
            String gender,
            Float height,
            Float weight,
            Float bmi,
            Integer isSmoker,
            Integer isDrinker,
            Integer hasChronicDisease,
            String chronicDiseaseList,
            String surgeryHistory,
            String bloodPressure,
            String bloodSugar
    ) {
        return new UserHealth(
                user, userName, age, gender, height, weight, bmi,
                isSmoker, isDrinker, hasChronicDisease,
                chronicDiseaseList, surgeryHistory,
                bloodPressure, bloodSugar
        );
    }

    // (D) --------------- update 메서드 --------------- //
    public void updateHealth(
            String userName,
            Integer age,
            String gender,
            Float height,
            Float weight,
            Float bmi,
            Integer isSmoker,
            Integer isDrinker,
            Integer hasChronicDisease,
            String chronicDiseaseList,
            String surgeryHistory,
            String bloodPressure,
            String bloodSugar
    ) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.isSmoker = isSmoker;
        this.isDrinker = isDrinker;
        this.hasChronicDisease = hasChronicDisease;
        this.chronicDiseaseList = chronicDiseaseList;
        this.surgeryHistory = surgeryHistory;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
    }
}
