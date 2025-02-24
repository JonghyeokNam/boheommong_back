package com.sesac.boheommong.domain.notification.swagger;

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
        summary = "알림 SSE 구독",
        description = "알림을 실시간으로 수신하기 위해 SSE 연결을 맺는다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "SSE 구독 성공 (스트리밍 시작)",
                content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)
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
public @interface SubscribeNotification {
}
