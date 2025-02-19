package com.sesac.boheommong.domain.notification.entity;

import com.sesac.boheommong.domain.notification.enums.NotificationType;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_deleted = true, deleted_at = now() WHERE notification_id = ?")
@SQLRestriction("is_deleted = FALSE")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String content;

    private boolean checked;

    @Builder
    public Notification(User receiver, NotificationType notificationType, String content) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.checked = false;
    }

    public void isRead() { checked = true; }

    // public void delete() { this.getIsDeleted(); }
}
