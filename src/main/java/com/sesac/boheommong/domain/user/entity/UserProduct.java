package com.sesac.boheommong.domain.user.entity;

import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: 어느 유저가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // user 테이블의 PK
    private User user;

    // 예: 어떤 상품을
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // 추가 필드 (예: 결제 금액, 구매 일시 등)
    private Long paidAmount; // 결제 금액
}
