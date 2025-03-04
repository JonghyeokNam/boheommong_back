package com.sesac.boheommong.domain.notification.service;

import com.sesac.boheommong.domain.notification.dto.NotificationResponseDto;
import com.sesac.boheommong.domain.notification.entity.Notification;
import com.sesac.boheommong.domain.notification.enums.NotificationType;
import com.sesac.boheommong.domain.notification.repository.EmitterRepository;
import com.sesac.boheommong.domain.notification.repository.NotificationRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import com.sesac.boheommong.global.response.Response;
import com.sesac.boheommong.infra.redis.NotificationMessage;
import com.sesac.boheommong.infra.redis.NotificationPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationPublisher notificationPublisher;

    // 연결 지속시간 한시간
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(String loginEmail, String lastEventId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 고유한 아이디 생성
        Long userId = user.getUserId();
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 시간 초과나 비동기 요청이 안되면 자동으로 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 최초 연결 시 더미데이터가 없으면 503 오류가 발생하기 때문에 해당 더미 데이터 생성
        sendToClient(emitter,emitterId, "EventStream Created. [userId=" + userId + "]");

        // SSE의 자동 재연결 기능 때문에 재연결시 lastEventId를 기준으로 이후의 이벤트들만 전송
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheByUserId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    public void send(String loginEmail, NotificationType notificationType, String content, String url) {
        User receiver = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));
        String userId = String.valueOf(receiver.getUserId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterByUserId(userId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    NotificationResponseDto dto = NotificationResponseDto.from(notification);
                    Response<NotificationResponseDto> response = Response.success(dto);
                    sendToClient(emitter, key, response);
                }
        );
    }

    public List<NotificationResponseDto> getAllNotifications(String loginEmail) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        List<Notification> notifications = notificationRepository.findByReceiverAndIsDeletedFalse(user);
        // 혹은 @Where(is_deleted=false) / @SQLRestriction 활용 시, 단순 findByReceiver(user) 가능

        return notifications.stream()
                .map(NotificationResponseDto::from)
                .toList();
    }

    public void publishNotification(Long receiverId, String content, String url) {
        // (1) 메시지 DTO 생성
        NotificationMessage msg = new NotificationMessage();
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setUrl(url);
        msg.setNotificationType(NotificationType.AUTOPAYMENT);

        // (2) Redis에 Publish
        notificationPublisher.publish(msg);
    }

    private Notification createNotification(User receiver, NotificationType notificationType, String content, String url) {
        return  Notification.builder().receiver(receiver).notificationType(notificationType).content(content).url(url).build();
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw BaseException.from(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }

    public void readNotification(String loginEmail, Long notificationId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> BaseException.from(ErrorCode.NOTIFICATION_NOT_FOUND));

        // 알림의 수신자와 현재 유저가 같은지 검증(옵션)
        if (!notification.getReceiver().equals(user)) {
            throw BaseException.from(ErrorCode.INVALID_PERMISSION);
        }

        // 읽음 처리
        notification.isRead(); // or notification.markAsRead();
        // JPA 영속성 컨텍스트 내에서 상태값이 변경되므로, 별도의 save 필요 없이 트랜잭션 종료 시점에 반영
    }

    /** ======================
     *   알림 삭제(소프트 삭제)
     *  ====================== */
    public void deleteNotification(String loginEmail, Long notificationId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> BaseException.from(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().equals(user)) {
            throw BaseException.from(ErrorCode.INVALID_PERMISSION);
        }

        // soft-delete
        notificationRepository.delete(notification);
        // @SQLDelete(sql="UPDATE notifications SET is_deleted = true, deleted_at=now()...") 에 의해 실제로는 UPDATE로 처리
    }

    public void sendByRedisMessage(NotificationMessage msg) {
        // 1) DB 저장
        User receiver = userRepository.findById(msg.getReceiverId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = notificationRepository.save(
                new Notification(receiver, msg.getNotificationType(), msg.getContent(), msg.getUrl())
        );

        // 2) SSE로 전송
        String userId = String.valueOf(receiver.getUserId());
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterByUserId(userId);

        sseEmitters.forEach((key, emitter) -> {
            try {
                // (옵션) 캐시에 이벤트 저장, 재연결 시 누락 이벤트 전송 용도
                emitterRepository.saveEventCache(key, notification);

                // DTO 변환
                NotificationResponseDto dto = NotificationResponseDto.from(notification);
                // 보통 공통 Response 래퍼를 쓰거나, dto 자체를 data로 전송
                Response<NotificationResponseDto> response = Response.success(dto);

                // 실제 SSE 이벤트 전송
                emitter.send(
                        SseEmitter.event()
                                .id(key)              // 이벤트 ID
                                .name("notification") // 이벤트 명
                                .data(response)       // 보내고자 하는 데이터
                );

            } catch (IOException e) {
                // 전송 실패 시 Emitter 제거
                emitterRepository.deleteById(key);
            }
        });
    }
}
