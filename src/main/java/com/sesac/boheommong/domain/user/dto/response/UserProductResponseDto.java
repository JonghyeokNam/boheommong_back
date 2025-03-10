package com.sesac.boheommong.domain.user.dto.response;

import com.sesac.boheommong.domain.user.entity.UserProduct;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProductResponseDto {
    private Long id;
    private Long productId;
    private Long paidAmount;

    // 필요하다면 유저 정보도 일부 노출 가능
    private String userName;
    private String userEmail;

    // 엔티티 -> DTO 변환
    public static UserProductResponseDto fromEntity(UserProduct userProduct) {
        return UserProductResponseDto.builder()
                .id(userProduct.getId())
                .productId(userProduct.getProductId())
                .paidAmount(userProduct.getPaidAmount())
                // user가 Lazy인 경우, Service 계층에서 이미 초기화되도록 fetch join을 쓰거나,
                // 같은 트랜잭션 내에서 user.getName() / user.getLoginEmail()에 접근해야 함
                .userName(userProduct.getUser().getName())
                .userEmail(userProduct.getUser().getLoginEmail())
                .build();
    }
}
