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
        summary = "알림 읽음 처리",
        description = "특정 알림을 읽은 상태로 변경한다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "알림 읽음 처리 성공"
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
public @interface ReadNotification {
}
