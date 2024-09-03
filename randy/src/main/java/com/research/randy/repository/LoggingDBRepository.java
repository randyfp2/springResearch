package com.research.randy.repository;

import com.research.randy.model.logging.LoggingModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggingDBRepository extends JpaRepository<LoggingModel, String> {
}
