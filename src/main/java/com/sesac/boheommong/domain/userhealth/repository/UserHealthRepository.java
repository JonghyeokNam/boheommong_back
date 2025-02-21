package com.sesac.boheommong.domain.userhealth.repository;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHealthRepository extends JpaRepository<UserHealth, Long> {
    Optional<UserHealth> findByUser(User user);
}
