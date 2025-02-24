package com.sesac.boheommong.domain.notification.dto;

import com.sesac.boheommong.domain.notification.entity.Notification;
import com.sesac.boheommong.domain.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long notificationId;           // 알림 PK
    private NotificationType notificationType;
    private String content;
    private boolean checked;

    // 필요하다면 날짜 정보(생성일)나 Receiver(유저 정보) 등을 추가
    private LocalDateTime createdAt;
    private String receiver;
    private String url;

    /**
     * Notification 엔티티 -> NotificationResponse 변환 메서드
     */
    public static NotificationResponseDto from(Notification notification) {
        if (notification == null) {
            return null;
        }
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .checked(notification.isChecked())
                .createdAt(notification.getCreatedAt()) // BaseEntity에 있다면
                .receiver(notification.getReceiver().getName()) // 필요하다면
                .url(notification.getUrl())
                .build();
    }
}
