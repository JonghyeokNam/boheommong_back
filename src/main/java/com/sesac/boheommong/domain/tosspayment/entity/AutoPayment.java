package com.sesac.boheommong.domain.tosspayment.entity;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(
        name = "auto_payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "product_id"})
        }
)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private InsuranceProduct product;

    @Column(nullable = false)
    private Integer dayOfMonth;

    @Column(nullable = false)
    private String time;

    /**
     * private 생성자를 사용해 외부에서 직접 인스턴스를 만들 수 없게 막고,
     * 정적 팩토리 메서드로만 만들도록 유도
     */
    private AutoPayment(User user, InsuranceProduct product, Integer dayOfMonth, String time) {
        this.user = user;
        this.product = product;
        this.dayOfMonth = dayOfMonth;
        this.time = time;
    }

    /**
     * 정적 팩토리 메서드
     */
    public static AutoPayment create(User user, InsuranceProduct product, Integer dayOfMonth, String time) {
        return new AutoPayment(user, product, dayOfMonth, time);
    }

    public void updateDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
    public void updateTime(String time) {
        this.time = time;
    }
}

