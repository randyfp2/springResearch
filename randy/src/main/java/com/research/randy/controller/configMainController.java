package com.research.randy.controller;
import com.research.randy.model.configMain;
import com.research.randy.service.configMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/configs")

public class configMainController {

    private final configMainService newConfigMainService;

    @Autowired
    public configMainController(configMainService newConfigMainService) {
        this.newConfigMainService = newConfigMainService;
    }

    @GetMapping
    public List<configMain> getAllConfigs() {
        return newConfigMainService.getAllConfigs();
    }

    @GetMapping("/{key}")
    public ResponseEntity<configMain> getConfigByKey(@PathVariable String key) {
        Optional<configMain> newConfigMain = newConfigMainService.getConfigByKey(key);
        return newConfigMain.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<configMain> createOrUpdateConfig(@RequestBody configMain configMain) {
        configMain savedConfig = newConfigMainService.saveConfig(configMain);
        return ResponseEntity.ok(savedConfig);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String key) {
        if (newConfigMainService.getConfigByKey(key).isPresent()) {
            newConfigMainService.deleteConfig(key);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
