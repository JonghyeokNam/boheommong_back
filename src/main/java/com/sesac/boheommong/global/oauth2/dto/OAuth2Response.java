package com.sesac.boheommong.global.oauth2.dto;

import java.util.Map;

public interface OAuth2Response {

    String getProvider(); //제공자 (Ex. naver, google, ...)
    String getEmail(); //이메일
    String getName(); //사용자 실명 (설정한 이름)
    Map<String, Object> getAttribute();
}