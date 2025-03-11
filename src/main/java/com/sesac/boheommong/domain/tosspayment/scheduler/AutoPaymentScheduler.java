package com.sesac.boheommong.domain.tosspayment.scheduler;

import com.sesac.boheommong.domain.tosspayment.entity.AutoPayment;
import com.sesac.boheommong.domain.tosspayment.repository.AutoPaymentRepository;
import com.sesac.boheommong.domain.notification.service.NotificationService;
import com.sesac.boheommong.domain.notification.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoPaymentScheduler {

    private final AutoPaymentRepository autoPaymentRepository;
    private final NotificationService notificationService;

    /**
     * 매일 오전 0시(혹은 원하는 시각)에 실행
     * - “내일 자동결제 예정”인 AutoPayment 찾아서, 알림 발행
     */

//    @Scheduled(cron = "0 */3 * * * *")
//    @Scheduled(cron = "0 */3 * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void notifyBeforeAutoPayment() {
        log.info("자동결제 전날 알림 스케줄러 실행...");

        // (1) 오늘 날짜 계산
        LocalDate today = LocalDate.now();

        // (2) "내일" 자동이체가 예정된 AutoPayment 찾기
        //  - dayOfMonth == tomorrow's day
        //  - time은 결제 시각이지만, 전날 알림이므로 여기서는 dayOfMonth만 체크
        int tomorrowDay = today.plusDays(1).getDayOfMonth();

        // dayOfMonth가 tomorrowDay인 AutoPayment 목록 (DB 쿼리 방식은 상황에 따라 다름)
        List<AutoPayment> autoPayments = autoPaymentRepository.findByDayOfMonth(tomorrowDay);

        // (3) 알림 발행
        for (AutoPayment ap : autoPayments) {
            Long userId = ap.getUser().getUserId();

            String productName = ap.getProduct().getProductName(); // 상품 이름
            String content = String.format("내일 (%d일) 자동결제가 예정되어 있습니다. (상품명: %s)", tomorrowDay, productName);

            // Redis를 통해 발행(멀티 서버)
            notificationService.publishNotification(
                    userId,
                    content,
                    "/mypage/autoPayment"  // 알림 클릭 시 이동할 URL (예시)
            );

            log.info("자동결제 전날 알림 발행 - userId={}, autoPaymentId={}", userId, ap.getId());
        }
    }
}
