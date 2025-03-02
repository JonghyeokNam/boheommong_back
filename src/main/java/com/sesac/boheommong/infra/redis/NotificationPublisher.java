package com.sesac.boheommong.infra.redis;

public interface NotificationPublisher {
    void publish(NotificationMessage message);
}
