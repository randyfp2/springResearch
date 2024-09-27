package com.research.randy.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ISO8583ConverterToString {
    private static final Map<String, String> FIELD_SPECS = new LinkedHashMap<>();

    static {
        FIELD_SPECS.put("0. MESSAGE TYPE INDICATOR", "4;NUM");
        //FIELD_SPECS.put("1. BIT MAP, EXTENDED", "16;ALPHA");
        //FIELD_SPECS.put("2. PRIMARY ACCOUNT NUMBER", "(requested);NUM;19");
        FIELD_SPECS.put("3. PROCESSING CODE", "6;NUM");
        FIELD_SPECS.put("4. AMOUNT, TRANSACTION", "12;NUM");
        FIELD_SPECS.put("7. TRANSMISSION DATE AND TIME", "10;NUM");
        FIELD_SPECS.put("11. SYSTEM TRACE AUDIT NUMBER", "6;NUM");
        FIELD_SPECS.put("12. TIME, LOCAL TRANSACTION", "6;NUM");
        FIELD_SPECS.put("13. DATE, LOCAL TRANSACTION", "4;NUM");
        FIELD_SPECS.put("18. MERCHANT'S TYPE", "4;NUM");
        FIELD_SPECS.put("29. AMOUNT, SETTLEMENT FEE", "8;NUM");
        FIELD_SPECS.put("32. ACQUIRING INSTITUTION IDENTIFICATION CODE", "(requested);NUM;11");
        FIELD_SPECS.put("33. FORWARDING INSTITUTION IDENTIFICATION CODE", "(requested);NUM;11");
        FIELD_SPECS.put("37. RETRIEVAL REFERENCE NUMBER", "12;ALPHA");
        FIELD_SPECS.put("39. RESPONSE CODE", "2;ALPHA");
        FIELD_SPECS.put("41. CARD ACCEPTOR TERMINAL IDENTIFICATION", "8;ALPHA");
        FIELD_SPECS.put("42. CARD ACCEPTOR IDENTIFICATION CODE", "15;ALPHA");
        FIELD_SPECS.put("43. CARD ACCEPTOR NAME/LOCATION", "(requested);ALPHA;40");
        FIELD_SPECS.put("48. ADDITIONAL DATA - PRIVATE", "(requested);ALPHA;999");
        FIELD_SPECS.put("49. CURRENCY CODE, TRANSACTION", "3;NUM");
        FIELD_SPECS.put("52. PERSONAL IDENTIFICATION NUMBER (PIN)", "16;ALPHA");
        FIELD_SPECS.put("63. SERVICE CODE", "4;NUM");
        FIELD_SPECS.put("102. SOURCE ACCOUNT", "(requested);ALPHA;28");
        FIELD_SPECS.put("103. BENEFICIARY ACCOUNT", "(requested);ALPHA;28");
        FIELD_SPECS.put("104. TRANSACTION DESCRIPTION", "(requested);ALPHA;100");
        FIELD_SPECS.put("126. SOURCE INSTITUTION CODE", "(requested);NUM;6");
        FIELD_SPECS.put("127. BENEFICIARY INSTITUTION CODE", "(requested);NUM;6");
    }

    public Map<String, String> convertToISO8583(Map<String, String> jsonRequest) {
        StringBuilder isoRequest = new StringBuilder();
        StringBuilder formatRequest = new StringBuilder();
        Map<String, Integer> fieldLengths = new LinkedHashMap<>();
        int currentIndex = 0;

        for (Map.Entry<String, String> entry : FIELD_SPECS.entrySet()) {
            String fieldName = entry.getKey();
            String fieldSpec = entry.getValue();
            String fieldValue = jsonRequest.getOrDefault(fieldName, "");

            String[] specParts = fieldSpec.split(";");
            int length;

//            if (fieldValue.isEmpty()) {
//                // Jika kosong, teruskan ke iterasi berikutnya
//                continue;
//            }

            if (specParts[0].equals("(requested)")) { // Pengecekan untuk tanda (requested)
                length = fieldValue.length();

                int maxLength = Integer.parseInt(specParts[2]);
                if (length > maxLength) {
                    throw new IllegalArgumentException("Field " + fieldName + " exceeds maximum length of " + maxLength); // Pengecekan panjang maksimal
                }
                fieldSpec = length + ";" + specParts[1];
            } else {
                length = Integer.parseInt(specParts[0]);
            }
            String type = specParts[1];

            // Pad or truncate the field value
            if (length > 0) { // Pastikan length positif
                if (type.equals("NUM")) {
                    fieldValue = String.format("%" + length + "s", fieldValue).replace(' ', '0');
                } else {
                    fieldValue = String.format("%-" + length + "s", fieldValue);
                }
            } else {
                throw new IllegalArgumentException("Invalid length for field " + fieldName); // Tambahkan pengecekan untuk nilai kosong atau negatif
            }

            isoRequest.append(fieldValue);
            formatRequest.append(fieldSpec).append(";").append(fieldName).append("-");
            fieldLengths.put(fieldName, length);
        }

        // Remove the last '-' from formatRequest
        formatRequest.setLength(formatRequest.length() - 1);

        Map<String, String> result = new HashMap<>();
        result.put("isoRequest", isoRequest.toString());
        result.put("formatRequest", formatRequest.toString());
        result.put("fieldLengths", fieldLengths.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(";"))); // Simpan panjang field

        // Debug output
        System.out.println("ISO Request: " + isoRequest.toString());
        System.out.println("ISO Request Length: " + isoRequest.length());
        System.out.println("Format Request: " + formatRequest.toString());
        System.out.println("Field Lengths dfsdfdsf: " + fieldLengths);

        return result;
    }
}
