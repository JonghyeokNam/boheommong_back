package com.sesac.boheommong.domain.notification.swagger;

import com.sesac.boheommong.domain.notification.dto.NotificationResponseDto;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "알림 목록 조회",
        description = "현재 로그인 유저가 받은 알림 목록을 조회한다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "알림 목록 조회 성공",
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
public @interface GetAllNotifications {
}

