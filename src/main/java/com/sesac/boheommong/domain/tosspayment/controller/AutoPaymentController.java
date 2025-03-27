package com.sesac.boheommong.domain.tosspayment.controller;

import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.service.AutoPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/autoPayments")
public class AutoPaymentController {

    private final AutoPaymentService autoPaymentService;
    
    @Operation(summary = "자동결제 생성", description = "유저 ID, 상품 ID, 날짜, 시간을 받아 자동결제를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public AutoPaymentDto createAutoPayment(@RequestBody AutoPaymentDto dto) {
        return autoPaymentService.createAutoPayment(dto);
    }

    /**
     * 전체 목록 조회
     */
    @Operation(summary = "전체 자동결제 목록 조회", description = "DB에 있는 모든 자동결제 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public List<AutoPaymentDto> getAllAutoPayments() {
        return autoPaymentService.getAllAutoPayments();
    }

    /**
     * 특정 단건 상세조회
     */
    @Operation(summary = "자동결제 상세조회", description = "autoPaymentId로 특정 자동결제 정보를 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음")
    })
    @GetMapping("/{id}")
    public AutoPaymentDto getAutoPaymentDetail(@PathVariable Long id) {
        return autoPaymentService.getAutoPaymentDetail(id);
    }

    /**
     * 사용자별 자동결제 목록 조회
     */
    @Operation(summary = "사용자별 자동결제 목록 조회", description = "특정 사용자의 자동결제 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않음")
    })
    @GetMapping("/user/{userId}")
    public List<AutoPaymentDto> getAutoPaymentsByUser(@PathVariable Long userId) {
        return autoPaymentService.getAutoPaymentsByUser(userId);
    }

    /**
     * 자동결제 수정
     * dayOfMonth, time만 가능
     */
    @Operation(summary = "자동결제 수정", description = "autoPaymentId로 특정 자동결제를 수정합니다. (날짜, 시간만 변경 가능)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음")
    })
    @PutMapping("/{id}")
    public AutoPaymentDto updateAutoPayment(@PathVariable Long id, @RequestBody AutoPaymentDto dto) {
        return autoPaymentService.updateAutoPayment(id, dto);
    }

    /**
     * 자동결제 삭제
     */
    @Operation(summary = "자동결제 삭제", description = "autoPaymentId로 특정 자동결제 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공 (No Content)"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음")
    })
    @DeleteMapping("/{id}")
    public void deleteAutoPayment(@PathVariable Long id) {
        autoPaymentService.deleteAutoPayment(id);
    }
}
