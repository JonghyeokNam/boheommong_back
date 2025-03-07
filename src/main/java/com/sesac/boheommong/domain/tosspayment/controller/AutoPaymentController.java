package com.sesac.boheommong.domain.tosspayment.controller;

import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.service.AutoPaymentService;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AutoPaymentController {

    private final AutoPaymentService autoPaymentService;

    public AutoPaymentController(AutoPaymentService autoPaymentService) {
        this.autoPaymentService = autoPaymentService;
    }

    /**
     * 자동결제 생성
     *
     * <p>JSON 예시:
     * <pre>
     * {
     *   "userId": 10,
     *   "dayOfMonth": 26,
     *   "time": "11:00",
     *   "productId": 999
     * }
     * </pre>
     *
     * <p>productId가 넘어오면, 해당 보험상품과 연동하여 DB에 product_id 저장
     */
    @Operation(summary = "자동 결제 생성", description = "사용자의 자동 결제 정보를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/autoPayment")
    public Response<AutoPaymentDto> createAutoPayment(@RequestBody AutoPaymentDto dto) {
        AutoPaymentDto saved = autoPaymentService.createAutoPayment(dto);
        return Response.success(saved);
    }

    /**
     * 자동결제 목록 조회
     */
    @Operation(summary = "자동 결제 목록 조회", description = "등록된 자동 결제 정보들을 모두 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/autoPayment")
    public Response<List<AutoPaymentDto>> getAutoPayments() {
        List<AutoPaymentDto> list = autoPaymentService.getAllAutoPayments();
        return Response.success(list);
    }

    /**
     * 자동결제 단건 상세조회
     */
    @Operation(summary = "자동 결제 상세조회", description = "주어진 id로 자동 결제 정보를 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/autoPayment/{id}")
    public Response<AutoPaymentDto> getAutoPaymentDetail(@PathVariable Long id) {
        AutoPaymentDto detail = autoPaymentService.getAutoPaymentDetail(id);
        return Response.success(detail);
    }

    /**
     * 자동결제 수정
     *
     * <p>JSON 예시:
     * <pre>
     * {
     *   "dayOfMonth": 15,
     *   "time": "09:30",
     *   "productId": 123
     * }
     * </pre>
     *
     * <p>productId를 지정하면 해당 보험상품으로 수정하여 연관관계를 설정
     * <p>productId가 null이면 기존 연관관계 해제
     */
    @Operation(summary = "자동 결제 수정", description = "주어진 id의 자동 결제 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/autoPayment/{id}")
    public Response<AutoPaymentDto> updateAutoPayment(
            @PathVariable Long id,
            @RequestBody AutoPaymentDto dto
    ) {
        AutoPaymentDto updated = autoPaymentService.updateAutoPayment(id, dto);
        return Response.success(updated);
    }

    /**
     * 자동결제 삭제
     */
    @Operation(summary = "자동 결제 삭제", description = "주어진 id의 자동 결제 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공 (No Content)"),
            @ApiResponse(responseCode = "404", description = "해당 정보가 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/autoPayment/{id}")
    public Response<Void> deleteAutoPayment(@PathVariable Long id) {
        autoPaymentService.deleteAutoPayment(id);
        // 응답 바디가 필요 없다면 null 을 넘기거나, 별도 메시지를 넣어도 됨
        return Response.success(null);
    }
}
