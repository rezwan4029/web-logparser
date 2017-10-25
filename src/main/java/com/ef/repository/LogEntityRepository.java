package com.ef.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ef.schema.LogEntity;


public interface LogEntityRepository extends JpaRepository<LogEntity, Integer> {

}
