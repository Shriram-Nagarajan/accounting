package com.um.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.um.entity.ForgotPasswordToken;

@Repository
public interface ForgotPwdTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
	
	List<ForgotPasswordToken> findByToken(String token);

}
