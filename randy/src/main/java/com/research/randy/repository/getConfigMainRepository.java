package com.research.randy.repository;

import com.research.randy.model.config.getConfigMain;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface getConfigMainRepository  extends JpaRepository<getConfigMain, String> {

    @Query("SELECT c.value FROM getConfigMain c WHERE c.key = :key")
    String findValueByKeyGroupAndIsEnable(@Param("key") String key);

}