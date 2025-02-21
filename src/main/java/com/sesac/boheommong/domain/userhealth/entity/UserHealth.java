package com.sesac.boheommong.domain.userhealth.entity;

import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
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
    private Integer isSmoker;

    @Column(name = "is_drinker")
    private Integer isDrinker;

    @Column(name = "has_chronic_disease")
    private Integer hasChronicDisease;

    @Column(name = "chronic_disease_list", length = 255)
    private String chronicDiseaseList;

    @Column(name = "surgery_history", length = 255)
    private String surgeryHistory;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "blood_sugar", length = 20)
    private String bloodSugar;
}
