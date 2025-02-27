package com.sesac.boheommong.domain.insurance.spec;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class InsuranceProductSpec {

    /**
     * [withFilter]
     *  회사명(여러개), 카테고리(여러개), 상품명 검색
     *  모두 null/empty면 조건 무시.
     */
    public static Specification<InsuranceProduct> withFilter(
            List<String> companyNames,
            List<InsuranceType> categories,
            String productName
    ) {
        return (root, query, cb) -> {
            // 기본조건: isDeleted = false
            var predicate = cb.isFalse(root.get("isDeleted"));

            // 1) companyNames in (...)
            if (companyNames != null && !companyNames.isEmpty()) {
                var inExp = root.get("companyName").in(companyNames);
                predicate = cb.and(predicate, inExp);
            }

            // 2) categories in (...)
            if (categories != null && !categories.isEmpty()) {
                var catIn = root.get("productCategory").in(categories);
                predicate = cb.and(predicate, catIn);
            }

            // 3) productName LIKE %...%
            if (productName != null && !productName.isBlank()) {
                var likeExp = cb.like(
                        cb.lower(root.get("productName")),
                        "%" + productName.toLowerCase() + "%"
                );
                predicate = cb.and(predicate, likeExp);
            }

            return predicate;
        };
    }
}
