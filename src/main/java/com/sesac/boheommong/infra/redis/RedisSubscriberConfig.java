package com.sesac.boheommong.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisSubscriberConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisNotificationSubscriber subscriber;
    private final ChannelTopic notificationTopic;

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        // subscriber를 등록
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        // 특정 채널 구독
        container.addMessageListener(messageListenerAdapter(), notificationTopic);
        return container;
    }
}

