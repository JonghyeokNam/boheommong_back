package com.sesac.boheommong.domain.user.controller;

import com.sesac.boheommong.domain.user.dto.request.UserRequestDto;
import com.sesac.boheommong.domain.user.dto.response.UserProductResponseDto;
import com.sesac.boheommong.domain.user.dto.response.UserResponseDto;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.entity.UserProduct;
import com.sesac.boheommong.domain.user.service.UserProductService;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.jwt.service.TokenService;
import com.sesac.boheommong.global.oauth2.util.CookieUtil;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserProductService userProductService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private static final String REFRESH_TOKEN_KEY_NAME = "refresh_token";

    @Operation(
            summary = "사용자 정보 저장",
            description = "로그인된 사용자의 정보를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 저장 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PutMapping
    public Response<UserResponseDto> saveInfo(HttpServletRequest request, @RequestBody UserRequestDto UserRequestDto) {
        return Response.success(userService.updateUser(tokenProvider.getUserLoginEmail(request), UserRequestDto));
    }

    @Operation(
            summary = "로그인된 사용자 정보 조회",
            description = "현재 로그인된 사용자의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @GetMapping
    public Response<UserResponseDto> getLoginUser(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        UserResponseDto dto = UserResponseDto.toDto(user);
        return Response.success(dto);
    }


    @Operation(
            summary = "로그아웃",
            description = "로그아웃 처리 후 Refresh Token 삭제 및 쿠키 제거를 수행합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        tokenService.deleteRefreshToken(tokenProvider.getUserLoginEmail(request));
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_KEY_NAME);
        return Response.success();
    }

    @Operation(
            summary = "신규 유저 확인 API",
            description = "신규 유저의 경우 true, 기존 유저의 경우 false 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @GetMapping("/check")
    public Response<Boolean> checkNewUser(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        boolean isNew = userService.getCheckNewUser(loginEmail);
        return Response.success(isNew);
    }

    // =============================================================
    //   (신규) 상품 구매 등록 / 구매 목록 조회
    // =============================================================

    @Operation(
            summary = "사용자 상품 구매 등록",
            description = "결제 후, 사용자가 특정 상품을 구매했음을 userProduct 테이블에 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "구매 등록 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping("/products/purchase")
    public Response<UserProduct> purchaseProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("paidAmount") Long paidAmount,
            @RequestHeader(name = "Authorization", required = false) String token,
            HttpServletRequest request
    ) {
        String userEmail = tokenProvider.getUserLoginEmail(request);
        UserProduct created = userProductService.createUserProduct(userEmail, productId, paidAmount);
        return Response.success(created);
    }

    @Operation(
            summary = "사용자 구매 상품 목록 조회",
            description = "현재 로그인된 사용자가 구매한 상품 목록(UserProduct) 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @GetMapping("/products")
    public Response<List<UserProductResponseDto>> getMyProducts(HttpServletRequest request) {
        String userEmail = tokenProvider.getUserLoginEmail(request);
        // 엔티티 대신 DTO를 바로 반환
        List<UserProductResponseDto> list = userProductService.getUserProductsDto(userEmail);
        return Response.success(list);
    }
}


