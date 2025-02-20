package com.sesac.boheommong.domain.insuranceproduct.entity;

import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "insurance_products")
public class InsuranceProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "company_name", length = 50)
    private String companyName;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "product_category", length = 50)
    private String productCategory;

    @Lob
    @Column(name = "coverage_details")
    private String coverageDetails;

    @Column(name = "monthly_premium")
    private Integer monthlyPremium;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;
}
