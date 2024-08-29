package com.research.randy.service;

import com.research.randy.model.configMain;
import com.research.randy.repository.configMainRepository;
import com.research.randy.specification.configMainSpecification;
import jakarta.persistence.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class configMainService {
    private final configMainRepository newConfigMainRepository;
    private static final Logger loggerGeneral = LoggerFactory.getLogger("com.research.randy.general");
    @Autowired
    public configMainService(configMainRepository configMainRepository) {
        this.newConfigMainRepository = configMainRepository;
    }

    //@Cacheable(value = "configCache")
    public Optional<configMain> getConfigByKey(String key) {
        loggerGeneral.info("AMBIL NIH found for key: {}", key);
        return newConfigMainRepository.findById(key);
    }

    @CacheEvict(value = "configCache", key = "#configMain.key")
    public configMain saveConfig(configMain configMain) {
        return newConfigMainRepository.save(configMain);
    }

    @CacheEvict(value = "configCache", key = "#key")
    public void deleteConfig(String key) {
        newConfigMainRepository.deleteById(key);
    }

    public List<configMain> getAllConfigs() {
        return newConfigMainRepository.findAll();
    }
    /*
    //
    public List<configMain> getConfigs(String keygroup, boolean enabled) {
        Specification<configMain> spec;
        spec = Specification.where(configMainSpecification.hasKeyGroup(keygroup))
                .and(configMainSpecification.isEnabled());
        return configMainRepository.findAll(spec);
    }
    */

}
