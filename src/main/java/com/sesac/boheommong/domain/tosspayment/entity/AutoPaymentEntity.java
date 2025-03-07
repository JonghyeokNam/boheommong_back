package com.sesac.boheommong.domain.tosspayment.entity;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auto_payment")
public class AutoPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer dayOfMonth;
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // 외래키 이름 지정
    private InsuranceProduct insuranceProduct;
}
