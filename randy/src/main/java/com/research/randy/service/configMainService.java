package com.research.randy.service;

import com.research.randy.model.configMain;
import com.research.randy.repository.configMainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class configMainService {
    private final configMainRepository newConfigMainRepository ;

    @Autowired
    public configMainService(configMainRepository configMainRepository) {
        this.newConfigMainRepository = configMainRepository;
    }

    public List<configMain> getAllConfigs() {
        return newConfigMainRepository.findAll();
    }

    public Optional<configMain> getConfigByKey(String key) {
        return newConfigMainRepository.findById(key);
    }

    public configMain saveConfig(configMain configMain) {
        return newConfigMainRepository.save(configMain);
    }

    public void deleteConfig(String key) {
        newConfigMainRepository.deleteById(key);
    }

}
