package com.accounting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accounting.entity.UserAccountId;
import com.accounting.entity.UserAccountMapping;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountMapping, UserAccountId>{

//    @Query("SELECT ua FROM UserAccountMapping ua WHERE ua.userAccountId.userId = :userId")
//    public List<UserAccountMapping> findByUserId(@Param("userId") long userId);
    
    public Optional<UserAccountMapping> findById(UserAccountId id);

	
}
