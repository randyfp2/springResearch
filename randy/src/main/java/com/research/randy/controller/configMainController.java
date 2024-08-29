package com.research.randy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.randy.apiResponse.apiResponse;
import com.research.randy.apiResponse.util.apiResponseUtil;
import com.research.randy.model.configMain;
import com.research.randy.service.configMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/configs")
public class configMainController {
    private final configMainService newConfigMainService;
    private static final Logger loggerGeneral = LoggerFactory.getLogger("com.research.randy.general");
    private static final Logger loggerTransactional = LoggerFactory.getLogger("com.research.randy.transactional");
    private final ObjectMapper objectMapper = new ObjectMapper();  // dependency Jackson untuk konversi objek ke JSON

    @Autowired
    public configMainController(configMainService newConfigMainService) {
        this.newConfigMainService = newConfigMainService;
    }

    @GetMapping
    public ResponseEntity<apiResponse<List<configMain>>> getAllConfigs() {
        loggerGeneral.info("Received request to get all configs");

        List<configMain> configs = newConfigMainService.getAllConfigs();
        apiResponse<List<configMain>> response = apiResponseUtil.createSuccessResponse(configs);

        logTransaction("GET /api/configs", null, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    public ResponseEntity<apiResponse<configMain>> getConfigByKey(@PathVariable String key) {
        System.out.println("TESTMASUK /key");
        loggerGeneral.info("Received request to get config by key: {}", key);

        Optional<configMain> newConfigMain = newConfigMainService.getConfigByKey(key);
        apiResponse<configMain> response = newConfigMain
                .map(config -> {
                    loggerGeneral.info("Config found for key: {}", key);
                    return apiResponseUtil.createSuccessResponse(config);
                })
                .orElseGet(() -> {
                    loggerGeneral.warn("Config not found for key: {}", key);
                    return apiResponseUtil.createErrorResponse("Config not found");
                });

        logTransaction(String.format("GET /api/configs/%s", key), null, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<apiResponse<configMain>> createOrUpdateConfig(@RequestBody configMain configMain) {
        loggerGeneral.info("Received request to create/update config: {}", configMain);

        configMain savedConfig = newConfigMainService.saveConfig(configMain);
        apiResponse<configMain> response = apiResponseUtil.createSuccessResponse(savedConfig);

        logTransaction("POST /api/configs", configMain, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<apiResponse<Void>> deleteConfig(@PathVariable String key) {
        loggerGeneral.info("Received request to delete config by key: {}", key);

        apiResponse<Void> response;
        if (newConfigMainService.getConfigByKey(key).isPresent()) {
            newConfigMainService.deleteConfig(key);
            loggerGeneral.info("Successfully deleted config with key: {}", key);
            response = apiResponseUtil.createSuccessResponse(null);
        } else {
            loggerGeneral.warn("Config not found for deletion with key: {}", key);
            response = apiResponseUtil.createErrorResponse("Config not found");
        }

        logTransaction(String.format("DELETE /api/configs/%s", key), null, response);
        return ResponseEntity.ok(response);
    }

    private void logTransaction(String action, Object requestPayload, Object responsePayload) {
        try {
            String requestJson = requestPayload != null ? objectMapper.writeValueAsString(requestPayload) : "null";
            String responseJson = responsePayload != null ? objectMapper.writeValueAsString(responsePayload) : "null";

            loggerTransactional.info("Action: {}, Request Payload: {}, Response Payload: {}", action, requestJson, responseJson);
        } catch (JsonProcessingException e) {
            loggerGeneral.error("Failed to log transaction", e);
        }
    }
}