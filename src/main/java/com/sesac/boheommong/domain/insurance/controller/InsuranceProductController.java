package com.sesac.boheommong.domain.insurance.controller;

import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductDetailResponseDto;
import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductResponseDto;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.service.InsuranceProductService;
import com.sesac.boheommong.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
     * 예:
     *   GET /api/insurance-products/search?page=1&size=10
     *       &companyNames=삼성화재&companyNames=DB손해보험
     *       &categories=CANCER&categories=LIFE
     *       &productName=암
     *
     *  page=1 -> 첫 페이지, page=2 -> 두 번째 페이지(내부적으로 0-based 변환)
     *  companyNames, categories -> 체크박스 선택(정확 매칭 IN)
     *  productName -> 선택적 검색(부분 검색 적용 시 Spec에서 cb.like() 사용)
     */
    @Operation(
            summary = "보험상품 목록 조회 (페이징 + 체크박스 필터)",
            description = """
            회사명(배열), 카테고리(배열), 상품명(문자열)로 검색 & 페이징 처리.
            <p>page=1부터 시작 (내부적으로 page-1 하여 0-based로 변환).
            <p>companyNames, categories는 정확 매칭(IN) 방식 (체크박스).
            <p>상품명은 필요 시 부분 검색 적용 가능 (Specification).
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/search")
    public Response<Page<InsuranceProductResponseDto>> searchInsuranceProducts(

            @Parameter(name = "page", description = "1-based 페이지 번호 (1=첫 페이지)", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "1") int page,

            @Parameter(name = "size", description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "10") int size,

            @Parameter(
                    name = "companyNames",
                    description = "회사명 리스트(정확 매칭 IN). 체크박스로 여러 개 선택 가능 (예: 삼성화재, DB손해보험).",
                    in = ParameterIn.QUERY,
                    example = "삼성화재"
            )
            @RequestParam(required = false)
            List<String> companyNames,

            @Parameter(
                    name = "categories",
                    description = "카테고리 리스트(정확 매칭 IN). 체크박스로 여러 개 선택 (CANCER, LIFE, SURGERY 등).",
                    in = ParameterIn.QUERY,
                    example = "CANCER"
            )
            @RequestParam(required = false)
            List<InsuranceType> categories,

            @Parameter(
                    name = "productName",
                    description = "상품명 검색 키워드 (선택).",
                    in = ParameterIn.QUERY,
                    example = "암"
            )
            @RequestParam(required = false)
            String productName
    ) {
        // 1) 1-based → 0-based 변환
        int zeroBasedPage = (page <= 0) ? 0 : (page - 1);

        // 2) Service 호출
        Page<InsuranceProductResponseDto> result =
                productService.searchProducts(zeroBasedPage, size, companyNames, categories, productName);

        // 3) 응답
        return Response.success(result);
    }
    @Operation(
            summary = "보험상품 상세조회",
            description = "주어진 productId로 보험상품 정보를 상세조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{productId}")
    public Response<InsuranceProductDetailResponseDto> getInsuranceProductDetail(
            @PathVariable Long productId
    ) {
        // Service 호출
        InsuranceProductDetailResponseDto detailDto = productService.getInsuranceProductDetail(productId);

        // 커스텀 Response 객체로 감싸서 반환 (ex: Response.success(data))
        return Response.success(detailDto);
    }
}
