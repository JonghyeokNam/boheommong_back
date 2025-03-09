package com.sesac.boheommong.domain.bookmark.swagger;

import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;
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
        summary = "로그인 사용자의 북마크 전체 조회",
        description = "사용자가 북마크한 모든 상품 목록을 최신순으로 조회한다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "북마크 목록 조회 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        // 목록 형태(List<BookmarkResponseDto>)를 표현하려면 아래처럼 배열로 표시:
                        schema = @Schema(type = "array", implementation = BookmarkResponseDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "인증 실패 (JWT 토큰 불량 등)",
                content = @Content(schema = @Schema(implementation = Response.class))
        )
})
public @interface GetAllBookmarks {
}
