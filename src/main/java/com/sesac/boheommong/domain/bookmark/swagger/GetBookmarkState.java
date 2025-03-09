package com.sesac.boheommong.domain.bookmark.swagger;

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
        summary = "특정 상품의 북마크 여부 확인",
        description = "로그인 사용자가 특정 상품에 대해 북마크했는지 여부를 조회한다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "북마크 여부 조회 성공 (true/false)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Boolean.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (파라미터 누락 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "해당 유저나 상품이 존재하지 않을 경우",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        )
})
public @interface GetBookmarkState {
}
