package com.sesac.boheommong.domain.userhealthinfo.repository;

import com.sesac.boheommong.domain.userhealthinfo.entity.UserHealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHealthInfoRepository extends JpaRepository<UserHealthInfo, Long> {
}
