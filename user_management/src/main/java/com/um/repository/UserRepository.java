package com.um.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.um.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	List<UserEntity> findByLoginId(String loginId);
	
	List<UserEntity> findByEmailId(String emailId);
	
}
