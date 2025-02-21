package com.sesac.boheommong.global.jwt.controller;

import com.sesac.boheommong.global.jwt.service.TokenService;
import com.sesac.boheommong.global.oauth2.util.CookieUtil;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 차현철
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "새로운 Access Token 생성",
            description = "Refresh Token을 사용하여 새로운 Access Token을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "새로운 Access Token 생성 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping
    public Response<Void> createNewAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookie(request, "refresh_token")
                        .map(Cookie::getValue)
                        .orElse(null);

        String newAccessToken = tokenService.createNewAccessToken(refreshToken);

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return Response.success();
    }
}
