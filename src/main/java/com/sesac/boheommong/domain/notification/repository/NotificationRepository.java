package com.sesac.boheommong.domain.notification.repository;

import com.sesac.boheommong.domain.notification.entity.Notification;
import com.sesac.boheommong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverAndIsDeletedFalse(User receiver);
}
