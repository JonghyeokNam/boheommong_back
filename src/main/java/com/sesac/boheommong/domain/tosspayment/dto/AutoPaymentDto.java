package com.sesac.boheommong.domain.tosspayment.dto;

import com.sesac.boheommong.domain.tosspayment.entity.AutoPaymentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AutoPaymentDto {
    private Long id;
    private Long userId;
    private Integer dayOfMonth;
    private String time;

    // [중요] 어떤 보험 상품과 연결할지 식별하기 위해 productId 추가
    private Long productId;

    public static AutoPaymentDto fromEntity(AutoPaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        AutoPaymentDto dto = new AutoPaymentDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDayOfMonth(entity.getDayOfMonth());
        dto.setTime(entity.getTime());

        // 보험상품이 연결되어 있다면 productId 세팅
        if (entity.getInsuranceProduct() != null) {
            dto.setProductId(entity.getInsuranceProduct().getProductId());
        }

        return dto;
    }
}
