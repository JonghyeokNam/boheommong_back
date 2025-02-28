package com.sesac.boheommong.infra.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.boheommong.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // (1) 역직렬화
            String publishedString = new String(message.getBody(), StandardCharsets.UTF_8);
            // JSON -> NotificationMessage DTO
            NotificationMessage msg = new ObjectMapper().readValue(publishedString, NotificationMessage.class);

            // (2) 실제 알림 로직 수행
            notificationService.sendByRedisMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

