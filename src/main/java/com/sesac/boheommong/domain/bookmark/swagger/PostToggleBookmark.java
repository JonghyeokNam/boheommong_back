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
        summary = "상품 북마크 추가/취소 토글",
        description = "이미 북마크가 없으면 추가, 이미 있다면 북마크 취소(소프트 삭제)."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "북마크 토글 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "해당 상품 또는 유저가 존재하지 않는 경우",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        )
})
public @interface PostToggleBookmark {
}
