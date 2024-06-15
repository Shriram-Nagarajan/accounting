package com.accounting.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accounting.auth.entity.PropertyEntity;


@Repository
public interface PropertiesRepository extends JpaRepository<PropertyEntity, String>{

}
