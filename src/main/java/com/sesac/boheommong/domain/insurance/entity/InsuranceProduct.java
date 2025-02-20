package com.sesac.boheommong.domain.insurance.entity;

import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "insurance_products")
@SQLDelete(sql = "UPDATE insurance_products SET is_deleted = true, deleted_at = now() WHERE product_id = ?")
@SQLRestriction("is_deleted = FALSE")
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
