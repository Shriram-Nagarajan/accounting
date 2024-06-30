package com.um.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.um.accounting.entity.UserAccountId;
import com.um.accounting.entity.UserAccountMapping;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountMapping, UserAccountId>{

}
