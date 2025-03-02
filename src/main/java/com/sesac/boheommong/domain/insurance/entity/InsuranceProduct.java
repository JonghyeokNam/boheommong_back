package com.sesac.boheommong.domain.insurance.entity;

import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * [InsuranceProduct 엔티티]
 *
 * - insurance_products 테이블
 * - 보험 상품명, 회사명, 보험료, 보장내용, 그리고 보험 유형(카테고리: InsuranceType)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    /**
     * [보험 카테고리 → Enum(InsuranceType)]
     *  - DB에는 문자열(EnumType.STRING)로 저장
     *  - 예: CANCER, SURGERY, DRIVER, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", length = 50)
    private InsuranceType productCategory;

    /**
     * 보장 내용 (TEXT)
     */
    @Lob
    @Column(name = "coverage_details")
    private String coverageDetails;

    /**
     * 월 보험료
     */
    @Column(name = "monthly_premium")
    private Integer monthlyPremium;

    public static InsuranceProduct create(
            String companyName,
            String productName,
            InsuranceType productCategory,
            String coverageDetails,
            Integer monthlyPremium
    ) {
        // 기본 생성자(private/protected)로 객체 만들고, 필드를 직접 할당
        InsuranceProduct product = new InsuranceProduct();
        product.companyName     = companyName;
        product.productName     = productName;
        product.productCategory = productCategory;
        product.coverageDetails = coverageDetails;
        product.monthlyPremium  = monthlyPremium;
        return product;
    }

    // 수정 로직이 필요하다면 별도 update 메서드
    public void updateCoverage(String newCoverage) {
        this.coverageDetails = newCoverage;
    }
}
