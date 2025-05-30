package com.sesac.boheommong.domain.bookmark.repository;

import com.sesac.boheommong.domain.bookmark.entity.Bookmark;
import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // List<Bookmark> findAllByUser_Id(Long userId);
    Optional<Bookmark> findByUserAndProduct(User user, InsuranceProduct product);

    List<Bookmark> findAllByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT b FROM Bookmark b " +
            "WHERE b.user.userId = :userId AND b.product.productId = :productId")
    Optional<Bookmark> findIncludingDeleted(@Param("userId") Long userId,
                                            @Param("productId") Long productId);
}
