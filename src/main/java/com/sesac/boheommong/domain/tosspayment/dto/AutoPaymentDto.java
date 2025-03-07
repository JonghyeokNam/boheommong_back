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

    public static AutoPaymentDto fromEntity(AutoPaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        AutoPaymentDto dto = new AutoPaymentDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDayOfMonth(entity.getDayOfMonth());
        dto.setTime(entity.getTime());
        return dto;
    }
}
