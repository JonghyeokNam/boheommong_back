package com.sesac.boheommong.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisNotificationPublisher implements NotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    @Override
    public void publish(NotificationMessage message) {
        // message를 직렬화하여 Publish
        redisTemplate.convertAndSend(notificationTopic.getTopic(), message);
    }
}

