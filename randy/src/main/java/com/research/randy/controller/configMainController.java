package com.research.randy.controller;
import com.research.randy.apiResponse.apiResponse;
import com.research.randy.apiResponse.util.apiResponseUtil;
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
    public ResponseEntity<apiResponse<List<configMain>>> getAllConfigs() {
        List<configMain> configs = newConfigMainService.getAllConfigs();
        return ResponseEntity.ok(apiResponseUtil.createSuccessResponse(configs));
    }

    @GetMapping("/{key}")
    public ResponseEntity<apiResponse<configMain>> getConfigByKey(@PathVariable String key) {
        Optional<configMain> newConfigMain = newConfigMainService.getConfigByKey(key);
        apiResponse<configMain> apiResponse = newConfigMain
                .map(apiResponseUtil::createSuccessResponse)
                .orElseGet(() -> apiResponseUtil.createErrorResponse("Config not found"));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<apiResponse<configMain>> createOrUpdateConfig(@RequestBody configMain configMain) {
        configMain savedConfig = newConfigMainService.saveConfig(configMain);
        apiResponse<configMain> apiResponse = apiResponseUtil.createSuccessResponse(savedConfig);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<apiResponse<Void>> deleteConfig(@PathVariable String key) {
        if (newConfigMainService.getConfigByKey(key).isPresent()) {
            newConfigMainService.deleteConfig(key);
            apiResponse<Void> apiResponse = apiResponseUtil.createSuccessResponse(null);
            return ResponseEntity.ok(apiResponse);
        } else {
            apiResponse<Void> apiResponse = apiResponseUtil.createErrorResponse("Config not found");
            return ResponseEntity.ok(apiResponse);
        }
    }
}
