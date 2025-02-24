package com.sesac.boheommong.global.oauth2.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("kakao_account");
    }

    @Override
    public Map<String, Object> getAttribute() {
        return attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        if (attribute == null) {
            return null;
        }

        return attribute.get("email").toString();
    }
    @Override
    public String getName() {
        if (attribute == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) attribute.get("profile");

        return profile.get("nickname").toString();
    }
}
