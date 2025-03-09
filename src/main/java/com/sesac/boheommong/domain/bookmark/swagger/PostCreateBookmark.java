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
        summary = "북마크 생성",
        description = "상품을 북마크한다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "북마크 생성 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BookmarkResponseDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "해당 상품 or 유저가 존재하지 않는 경우",
                content = @Content(schema = @Schema(implementation = Response.class))
        )
})
public @interface PostCreateBookmark {
}
