package com.sesac.boheommong.domain.userhealth.controller;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.domain.userhealth.dto.request.UserHealthRequestDto;
import com.sesac.boheommong.domain.userhealth.dto.response.UserHealthResponseDto;
import com.sesac.boheommong.domain.userhealth.service.UserHealthService;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * UserHealthController
 * - 사용자 건강정보 등록/수정/조회/삭제
 * - 카카오 로그인 -> tokenProvider.getUserLoginEmail(request)
 */
@RestController
@RequestMapping("/api/user-health")
@Slf4j
@RequiredArgsConstructor
public class UserHealthController {

    private final UserHealthService userHealthService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * [POST] 건강정보 등록
     */
    @Operation(
            summary = "건강정보 등록",
            description = "로그인된 사용자가 처음으로 건강정보를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "이미 존재할 경우 등 에러"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    public Response<UserHealthResponseDto> createHealth(
            HttpServletRequest request,
            @Valid @RequestBody UserHealthRequestDto dto
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        Long userId = user.getUserId();

        UserHealthResponseDto saved = userHealthService.createHealth(userId, dto);
        return Response.success(saved);
    }

    /**
     * [PUT] 건강정보 수정
     */
    @Operation(
            summary = "건강정보 수정",
            description = "이미 등록된 건강정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "건강정보가 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping
    public Response<UserHealthResponseDto> updateHealth(
            HttpServletRequest request,
            @Valid @RequestBody UserHealthRequestDto dto
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        Long userId = user.getUserId();

        UserHealthResponseDto updated = userHealthService.updateHealth(userId, dto);
        return Response.success(updated);
    }

    /**
     * [GET] 건강정보 조회
     */
    @Operation(
            summary = "건강정보 조회",
            description = "로그인된 사용자 본인의 건강정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "건강정보가 없음")
    })
    @GetMapping
    public Response<UserHealthResponseDto> getMyHealth(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        Long userId = user.getUserId();

        UserHealthResponseDto healthDto = userHealthService.getMyHealth(userId);
        return Response.success(healthDto);
    }

    /**
     * [DELETE] 건강정보 삭제
     */
    @Operation(
            summary = "건강정보 삭제",
            description = "로그인된 사용자의 건강정보를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 유저의 건강정보가 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @DeleteMapping
    public Response<Void> deleteHealth(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        User user = userService.getUserByLoginEmailOrElseThrow(loginEmail);
        Long userId = user.getUserId();

        userHealthService.deleteHealth(userId);
        return Response.success();
    }
}
