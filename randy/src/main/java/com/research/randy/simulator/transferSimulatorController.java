package com.research.randy.simulator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulator")
public class transferSimulatorController {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TRANSACTION_ID_LENGTH = 10;
    private static final String VALID_BENEFICIARY_ACCOUNT_NO = "7185000308";

    // Valid combinations for response
    private static final Map<String, String> VALID_COMBINATIONS = Map.of(
            "7185000308:7420532193", "00",
            "7185000309:7420532194", "01"
    );

    @PostMapping("/transferIntraBankV1")
    public Map<String, Object> getDynamicResponse(@RequestBody Map<String, Object> requestBody) {
        String beneficiaryAccountNo = (String) requestBody.get("beneficiary_account_no");
        String sourceAccountNo = (String) requestBody.get("source_account_no");

        String key = beneficiaryAccountNo + ":" + sourceAccountNo;

        // Response Map
        Map<String, Object> response = new LinkedHashMap<>();

        if (VALID_COMBINATIONS.containsKey(key)) {
            String responseCode = VALID_COMBINATIONS.get(key);

            response.put("response_code", responseCode);
            response.put("response_message", responseCode.equals("00") ? "Success" : "Additional Error");
            response.put("error_code", "SV-" + responseCode);
            response.put("error_message", responseCode.equals("00") ? "APPROVED" : "ADDITIONAL_ERROR");
            response.put("error_cat", responseCode.equals("00") ? "Success" : "Error");

            response.put("reference_no", generateFormattedReferenceNo());
            response.put("partner_reference_no", requestBody.get("partner_reference_no"));
            response.put("transaction_id", generateTransactionId());

            Map<String, String> amount = new LinkedHashMap<>();
            amount.put("value", (String) ((Map) requestBody.get("amount")).get("value"));
            amount.put("currency", (String) ((Map) requestBody.get("amount")).get("currency"));
            response.put("amount", amount);

            response.put("beneficiary_account_no", beneficiaryAccountNo);
            response.put("beneficiary_bank_code", "009"); // Placeholder value
            response.put("source_account_no", sourceAccountNo);
            response.put("receipt_number", "031110430132"); // Placeholder value
            response.put("customer_reference", "10052019"); // Placeholder value
            response.put("network_route", "0901"); // Placeholder value

            Map<String, String> additionalInfo = new LinkedHashMap<>();
            additionalInfo.put("device_id", (String) ((Map) requestBody.get("additional_info")).get("device_id"));
            additionalInfo.put("channel", (String) ((Map) requestBody.get("additional_info")).get("channel"));
            response.put("additional_info", additionalInfo);
        } else {
            response.put("response_code", "05");
            response.put("response_message", "Rekening Tidak di temukan");
            response.put("error_code", "SV-05");
            response.put("error_message", "BENEFICIARY ACCOUNT NOT FOUND");
            response.put("error_cat", "Error");
            response.put("reference_no", generateFormattedReferenceNo());
            response.put("partner_reference_no", requestBody.get("partner_reference_no"));
        }

        return response;
    }

    private String generateFormattedReferenceNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    private String generateTransactionId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("FT");
        for (int i = 0; i < TRANSACTION_ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}

