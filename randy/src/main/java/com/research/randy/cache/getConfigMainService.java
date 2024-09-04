package com.research.randy.cache;
import com.research.randy.model.config.getConfigMain;
import com.research.randy.repository.getConfigMainRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class getConfigMainService {
    @Autowired
    private getConfigMainRepository parameterRepository;
    @Cacheable(value = "parameters", key = "#key")
    public String getParameterValue(String key) {
        System.out.println("MASUK getConfigMainService");  // Logging hasil query
        String value = parameterRepository.findValueByKeyGroupAndIsEnable(key);
        System.out.println("Query result: " + value);  // Logging hasil query
        return value != null ? value : "Default Value";
    }
}
