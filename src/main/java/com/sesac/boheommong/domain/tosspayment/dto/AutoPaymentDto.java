package com.sesac.boheommong.domain.tosspayment.dto;

import com.sesac.boheommong.domain.tosspayment.entity.AutoPayment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AutoPayment DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AutoPaymentDto {

    private Long id;          // AutoPayment 엔티티의 PK
    private Long userId;      // FK (User PK)
    private Long productId;   // FK (InsuranceProduct PK)
    private Integer dayOfMonth;
    private String time;

    public static AutoPaymentDto fromEntity(AutoPayment entity) {
        if (entity == null) return null;

        AutoPaymentDto dto = new AutoPaymentDto();
        dto.setId(entity.getId());
        dto.setDayOfMonth(entity.getDayOfMonth());
        dto.setTime(entity.getTime());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getUserId());
        }
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getProductId());
        }

        return dto;
    }
}
