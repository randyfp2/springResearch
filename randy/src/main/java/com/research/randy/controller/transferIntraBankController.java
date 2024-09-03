package com.research.randy.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.randy.logging.LoggingService;
import com.research.randy.mappingService.transferIntraBankMappingService;
import com.research.randy.model.handler.errorResponseTemplate;
import com.research.randy.model.transferIntraBankApiResponse;
import com.research.randy.model.transferIntraBankRequest;
import com.research.randy.model.transferIntraBankResponse;
import com.research.randy.service.transferIntraBankService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("/restApi/esb/")
public class transferIntraBankController {

    @Autowired
    private transferIntraBankService transferService;
    private static final Logger loggerTransactional = LogManager.getLogger("com.research.randy.transactional");
    private static final Logger loggerGeneral = LogManager.getLogger("com.research.randy.general");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoggingService loggingService;
    @PostMapping("/transferIntraBank")
    public ResponseEntity<?> transferIntraBank(@RequestBody transferIntraBankRequest transferRequest) {
        try {
            //System.out.println("transferIntraBank Channel Request: " + transferRequest.toString()); //logging test sout
            String serviceUrl = "/restApi/esb/transferIntraBank";
            String incomingRequestTime = getCurrentTimestamp();
            String incomingRequestJson = objectMapper.writeValueAsString(transferRequest);
            // Log initial request
            //logTransaction(transferRequest, incomingRequestTime, null, null, null, null, null);

            // Mengirim request ke API eksternal dan mendapatkan respons yang sudah mapping
            transferIntraBankResponse mappedResponse = transferService.transfer(transferRequest);
            if (mappedResponse.getHttpStatus().equals("200 OK") && mappedResponse.getResponseCode().equals("00")) {
                // Mengembalikan respons yang telah dipetakan jika status 200
                //System.out.println("transferIntraBank Channel Response: " + mappedResponse.toString());
                // Catat waktu response keluar
                String incomingResponseJson = objectMapper.writeValueAsString(mappedResponse);
                // Log response
                logTransaction(
                        serviceUrl,
                        transferRequest,
                        incomingRequestJson,
                        incomingRequestTime,
                        transferService.getApiExternalUrl(),
                        transferService.getApiExternalRequest(),
                        transferService.getApiExternalResponse(),
                        transferService.getApiExternalHttpStatus(),
                        mappedResponse,
                        incomingResponseJson,
                        getCurrentTimestamp()
                );
                return ResponseEntity.ok(mappedResponse);
            } else if (mappedResponse.getHttpStatus().equals("200 OK")) {
                // Mengembalikan respons yang telah dipetakan jika status 200
                //System.out.println("transferIntraBank Channel Response: " + mappedResponse.toString());
                // Catat waktu response keluar
                String incomingResponseTime = getCurrentTimestamp();

                Map<String, Object> partialResponse = new LinkedHashMap<>();
                partialResponse.put("responseCode", mappedResponse.getResponseCode());
                partialResponse.put("responseMessage", mappedResponse.getResponseMessage());
                partialResponse.put("errorCode", mappedResponse.getErrorCode());
                partialResponse.put("errorMessage", mappedResponse.getErrorMessage());
                partialResponse.put("errorCat", mappedResponse.getErrorCat());
                partialResponse.put("referenceNo", mappedResponse.getReferenceNo());
                partialResponse.put("partnerReferenceNo", mappedResponse.getPartnerReferenceNo());
                partialResponse.put("additionalInfo", mappedResponse.getAdditionalInfo());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");


                String incomingResponseJson = objectMapper.writeValueAsString(partialResponse);
                // Log response
                logTransaction(
                        serviceUrl,
                        transferRequest,
                        incomingRequestJson,
                        incomingRequestTime,
                        transferService.getApiExternalUrl(),
                        transferService.getApiExternalRequest(),
                        transferService.getApiExternalResponse(),
                        transferService.getApiExternalHttpStatus(),
                        mappedResponse,
                        incomingResponseJson,
                        getCurrentTimestamp()
                );
                return new ResponseEntity<>(incomingResponseJson, headers, HttpStatus.OK);
            }
            // Mengembalikan respons error jika status bukan 200
            errorResponseTemplate errorResponse = createErrorResponse(
                    "91",
                    "LINK DOWN",
                    "SV-91",
                    "Cannot establish connection to SmartVista",
                    "systemError",
                    transferRequest.getPartnerReferenceNo());
            String incomingResponseJson = objectMapper.writeValueAsString(errorResponse);
            // Log response
            logTransaction(
                    serviceUrl,
                    transferRequest,
                    incomingRequestJson,
                    incomingRequestTime,
                    transferService.getApiExternalUrl(),
                    transferService.getApiExternalRequest(),
                    transferService.getApiExternalResponse(),
                    mappedResponse.getHttpStatus(),
                    mappedResponse,
                    incomingResponseJson,
                    getCurrentTimestamp()
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);

        } catch (Exception e) {
            // Mengembalikan respons dengan status 500 dan objek kosong jika terjadi kesalahan
            errorResponseTemplate errorResponse = createErrorResponse("99", "General Error", "SV-99", "Unexpected error occurred", "systemError", transferRequest.getPartnerReferenceNo());
            loggerGeneral.warn("transferIntraBank Exception: "+ e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private errorResponseTemplate createErrorResponse(String responseCode, String responseMessage, String errorCode, String errorMessage, String errorCat, String partnerReferenceNo) {
        errorResponseTemplate errorResponse = new errorResponseTemplate();
        errorResponse.setResponseCode(responseCode);
        errorResponse.setResponseMessage(responseMessage);
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setErrorCat(errorCat);
        errorResponse.setReferenceNo(generateFormattedReferenceNo()); // Generate unique reference number
        errorResponse.setPartnerReferenceNo(partnerReferenceNo);
        return errorResponse;
    }
    @Async
    private void logTransaction(
            String serviceUrl,
            transferIntraBankRequest transferRequest,
            String incomingRequestJson,
            String incomingRequestTime,
            String apiExternalURL,
            String apiExternalRequest,
            String apiExternalResponse,
            String apiExternalHttpStatus,
            transferIntraBankResponse mappedResponse,
            String incomingResponseJson,
            String incomingResponseTime
    ) {
        loggingService.logTransaction(
                serviceUrl,
                generateFormattedReferenceNo(),
                transferRequest.getPartnerReferenceNo(),
                mappedResponse != null ? mappedResponse.getResponseCode() : null,
                mappedResponse != null ? mappedResponse.getResponseMessage() : null,
                incomingRequestTime,
                incomingRequestJson,
                apiExternalURL,
                apiExternalRequest,
                apiExternalResponse,
                apiExternalHttpStatus,
                incomingResponseJson,
                incomingResponseTime
        );
    }
    private String generateFormattedReferenceNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    private String getCurrentTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}