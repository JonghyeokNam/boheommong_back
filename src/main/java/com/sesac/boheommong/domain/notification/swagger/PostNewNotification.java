package com.sesac.boheommong.domain.notification.swagger;

import com.sesac.boheommong.domain.notification.dto.NotificationResponseDto;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "새로운 알림 전송",
        description = "알림을 생성하고, 해당 수신자에게 SSE를 통해 보낸다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "알림 전송 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = NotificationResponseDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "4xx",
                description = "요청 처리 실패",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        )
})
public @interface PostNewNotification {
}
