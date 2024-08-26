package com.research.randy.repository;

import com.research.randy.model.configMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface configMainRepository extends JpaRepository<configMain, String> {
}
