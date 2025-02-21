package com.sesac.boheommong.domain.userhealth.repository;

import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHealthRepository extends JpaRepository<UserHealth, Long> {
}
