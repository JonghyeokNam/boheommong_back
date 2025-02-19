package com.sesac.boheommong.domain.insurance.entity;

import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "insurance_products")
@SQLDelete(sql = "UPDATE insurance_products SET is_deleted = true, deleted_at = now() WHERE product_id = ?")
@SQLRestriction("is_deleted = FALSE")
public class InsuranceProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    /**
     * ERD: company_id
     * - 다른 테이블(insurance_companies)과 연관관계( ManyToOne )를 맺을 수도 있지만,
     *   아직 Company 엔티티가 없다면 우선 Long 필드만 두고 직접 매핑해도 됩니다.
     */
    @NotNull
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_link", nullable = false)
    private String productLink;

    @Lob
    @Column(name = "coverage_details")
    private String coverageDetails;

    @NotNull
    @Column(name = "monthly_premium", precision = 10, scale = 2, nullable = false)
    private BigDecimal monthlyPremium;

    // 필요하다면 생성자 또는 정적 팩터리 메서드를 추가할 수 있습니다.
    // public InsuranceProduct(Long companyId, String productName, ...) { ... }
}

