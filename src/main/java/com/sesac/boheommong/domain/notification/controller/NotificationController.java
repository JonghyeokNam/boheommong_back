package com.sesac.boheommong.domain.notification.controller;

import com.sesac.boheommong.domain.notification.dto.NotificationResponseDto;
import com.sesac.boheommong.domain.notification.enums.NotificationType;
import com.sesac.boheommong.domain.notification.service.NotificationService;
import com.sesac.boheommong.domain.notification.swagger.*;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;

    /**
     * SSE 구독
     */
    @SubscribeNotification
    @GetMapping("/subscribe")
    public SseEmitter subscribe(
            HttpServletRequest request,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        // 1) 로그인 이메일 추출
        String loginEmail = tokenProvider.getUserLoginEmail(request);

        // 2) Service 호출
        return notificationService.subscribe(loginEmail, lastEventId);
    }

    /**
     * 알림 전송 (테스트용 등)
     */
    @PostNewNotification
    @PostMapping("/send")
    public Response<Void> sendNotification(
            HttpServletRequest request,
            @RequestParam NotificationType notificationType,
            @RequestParam String content,
            @RequestParam(required = false) String url
    ) {
        // 1) 로그인 이메일 추출
        String email = tokenProvider.getUserLoginEmail(request);

        // 2) Service 호출
        notificationService.send(email, notificationType, content, url);

        // 3) 공통 응답
        return Response.success();
    }

    /**
     * 알림 목록 조회
     */
    @GetAllNotifications
    @GetMapping
    public Response<List<NotificationResponseDto>> getAllNotifications(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        List<NotificationResponseDto> notificationList = notificationService.getAllNotifications(loginEmail);
        return Response.success(notificationList);
    }

    /**
     * 알림 읽음 처리
     */
    @ReadNotification
    @PostMapping("/{notificationId}/read")
    public Response<Void> readNotification(
            HttpServletRequest request,
            @PathVariable Long notificationId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        notificationService.readNotification(loginEmail, notificationId);
        return Response.success();
    }

    /**
     * 알림 삭제(소프트 삭제)
     */
    @DeleteNotification
    @PostMapping("/{notificationId}/delete")
    public Response<Void> deleteNotification(
            HttpServletRequest request,
            @PathVariable Long notificationId
    ) {
        String email = tokenProvider.getUserLoginEmail(request);
        notificationService.deleteNotification(email, notificationId);
        return Response.success();
    }
}
