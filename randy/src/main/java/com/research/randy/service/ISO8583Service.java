package com.research.randy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.randy.model.ISO8583.ISO8583Request;
import com.research.randy.model.ISO8583.ISO8583Response;
import com.research.randy.util.ISO8583ConverterToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ISO8583Service {
    @Autowired
    private ISO8583ConverterToString iso8583Converter;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SIMULATOR_URL = "http://localhost:5555/transfer/v1/transferBankSimulatorISO";

    public ISO8583Response processRequest(ISO8583Request request) {
        // Convert JSON to ISO 8583 format
        Map<String, String> convertedRequest = iso8583Converter.convertToISO8583(request.getIsoReq());

        // Prepare request for simulator
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("Administrator:manage".getBytes()));

        Map<String, String> body = new HashMap<>();
        body.put("isoRequest", convertedRequest.get("isoRequest"));
        body.put("formatRequest", convertedRequest.get("formatRequest"));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // Send request to simulator
            String simulatorResponse = restTemplate.postForObject(SIMULATOR_URL, entity, String.class);

            // Extract only the isoResponse from the simulator response
            JsonNode responseNode = objectMapper.readTree(simulatorResponse);
            String isoResponse = responseNode.get("isoResponse").asText();

            // Parse isoResponse to map using field lengths
            Map<String, Integer> fieldLengths = parseFieldLengths(convertedRequest.get("fieldLengths"));
            Map<String, String> parsedResponse = parseISOResponse(isoResponse, fieldLengths);

            // Create a new ISO8583Response with the parsed map
            ISO8583Response isoResponses = new ISO8583Response();
            isoResponses.setIsoRes(parsedResponse);

            return isoResponses;
        } catch (Exception e) {
            // Logging the error
            System.err.println("Error occurred while sending request to simulator: " + e.getMessage());
            e.printStackTrace();

            // Handle the error appropriately
            return null;
        }
    }

    private Map<String, Integer> parseFieldLengths(String fieldLengthsStr) {
        Map<String, Integer> fieldLengths = new LinkedHashMap<>();

        String[] fields = fieldLengthsStr.split(";\\s*");
        for (String field : fields) {
            String[] parts = field.split("=");
            if (parts.length == 2) {
                try {
                    fieldLengths.put(parts[0], Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for field: " + field);
                }
            } else {
                System.err.println("Invalid field format: " + field);
            }
        }
        return fieldLengths;
    }

    private Map<String, String> parseISOResponse(String isoResponse, Map<String, Integer> fieldLengths) {
        Map<String, String> parsedFields = new LinkedHashMap<>();
        int currentIndex = 0;

        for (Map.Entry<String, Integer> entry : fieldLengths.entrySet()) {
            String fieldName = entry.getKey();
            int length = entry.getValue();

            if (currentIndex + length <= isoResponse.length()) {
                String fieldValue = isoResponse.substring(currentIndex, currentIndex + length);
                parsedFields.put(fieldName, fieldValue.trim());
                currentIndex += length;
            } else {
                System.err.println("Index out of bounds for field: " + fieldName + " (Expected Length: " + length + ")");
                break;
            }
        }

        return parsedFields;
    }
}