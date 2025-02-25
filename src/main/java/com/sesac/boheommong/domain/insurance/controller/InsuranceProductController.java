package com.sesac.boheommong.domain.insurance.controller;

import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductResponseDto;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.service.InsuranceProductService;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance-products")
@RequiredArgsConstructor
public class InsuranceProductController {

    private final InsuranceProductService productService;

    /**
     * [GET] 페이징 + 다중필터(회사명, 카테고리) + 상품명 검색
     *  예)
     *   /api/insurance-products/search?page=0&size=10
     *   &companyNames=삼성화재&companyNames=현대해상
     *   &categories=CANCER&categories=DRIVER
     *   &productName=암
     */
    @Operation(summary="보험상품 목록(페이징 + 검색/필터)",
            description="회사(배열), 카테고리(배열), 상품명 검색 + 페이징")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="조회 성공")
    })
    @GetMapping("/search")
    public Response<Page<InsuranceProductResponseDto>> searchInsuranceProducts(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) List<String> companyNames,
            @RequestParam(required=false) List<InsuranceType> categories,
            @RequestParam(required=false) String productName
    ) {
        Page<InsuranceProductResponseDto> result =
                productService.searchProducts(page, size, companyNames, categories, productName);
        return Response.success(result);
    }
}
