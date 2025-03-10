package com.sesac.boheommong.infra.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sesac.boheommong.domain.notification.entity.Notification;
import com.sesac.boheommong.domain.notification.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationMessage {

    // 알림 ID (이미 DB에 저장된 경우)
    private Long notificationId;

    // 수신자 식별용 (User ID)
    private Long receiverId;

    // 알림 타입
    private NotificationType notificationType;

    // 알림 본문
    private String content;

    // 클릭 시 이동할 URL (옵션)
    private String url;

    // 읽음 여부 (옵션, 필요하다면)
    private boolean checked;

    /**
     * Notification 엔티티 -> NotificationMessage 변환 메서드
     */
    public static NotificationMessage from(Notification notification) {
        return NotificationMessage.builder()
                .notificationId(notification.getNotificationId())
                .receiverId(notification.getReceiver().getUserId()) // User 엔티티의 PK
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .url(notification.getUrl())
                .checked(notification.isChecked())
                .build();
    }
}

