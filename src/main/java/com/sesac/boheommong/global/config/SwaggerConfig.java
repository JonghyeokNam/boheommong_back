package com.sesac.boheommong.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * Swagger/OpenAPI 문서 설정
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // Bearer 인증 스키마 등록
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // 전역 보안 적용: 모든 API에 Bearer 인증을 추가(원하면 조정 가능)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // 문서 기본 정보
                .info(apiInfo());
    }

    /**
     * 문서 제목/설명/버전 등 기본 정보
     */
    private Info apiInfo() {
        return new Info()
                .title("API Test")                 // API 제목
                .description("Let's practice Swagger UI")  // API 설명
                .version("1.0.0");                // 버전
    }
}
