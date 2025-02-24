package com.sesac.boheommong.domain.userhealth.service;

import com.sesac.boheommong.domain.userhealth.dto.request.UserHealthRequest;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;

import java.util.Optional;

public interface UserHealthService {
    UserHealth createHealth(UserHealthRequest req);
    UserHealth updateHealth(UserHealthRequest req);
    Optional<UserHealth> findHealthByUserId(Long userId);
}
