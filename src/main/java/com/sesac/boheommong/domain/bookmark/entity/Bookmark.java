package com.sesac.boheommong.domain.bookmark.entity;

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
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id"})
})
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", nullable = false, updatable = false)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private InsuranceProduct product;

    private Bookmark(User user, InsuranceProduct product) {
        this.user = user;
        this.product = product;
    }

    public static Bookmark create(User user, InsuranceProduct product) {
        return new Bookmark(user, product);
    }
}
