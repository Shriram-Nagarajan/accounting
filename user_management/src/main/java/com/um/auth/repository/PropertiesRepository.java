package com.um.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.um.auth.entity.PropertyEntity;

@Repository
public interface PropertiesRepository extends JpaRepository<PropertyEntity, String>{

}
