package com.research.randy.simulator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/simulator")
public class transferSimulatorXmlController {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TRANSACTION_ID_LENGTH = 10;
    private static final String EXPECTED_USER_NAME = "superapp";
    private static final String EXPECTED_PASSWORD = "bsi123";
    // Valid account numbers
    private static final String[] VALID_DEBIT_ACCOUNTS = {"7340000007", "7340000008", "7340000009"};
    private static final String[] VALID_CREDIT_ACCOUNTS = {"7340000735", "7340000736", "7340000737"};
    private static final int MIN_DEBIT_AMOUNT = 10000;


    @PostMapping(value = "/transferIntraBankXml", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> simulateTransfer(@RequestBody String request) {
        String transactionId = generateTransactionId();
        String genReffNo = generateFormattedReferenceNo();
        // Extract userName and password from request
        String userName = extractFieldFromRequest(request, "userName");
        String password = extractFieldFromRequest(request, "password");

        // Extract fields from request
        String debitAccount = extractFieldFromRequest(request, "DebitAccount");
        String debitAmountStr = extractFieldFromRequest(request, "DebitAmount");
        String creditAccount = extractFieldFromRequest(request, "CreditAccount");

        // Static SOAP XML Response
        // Check if userName and password are correct
        if (!EXPECTED_USER_NAME.equals(userName) || !EXPECTED_PASSWORD.equals(password)) {
            String soapResponse =
                    "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "   <S:Body>" +
                            "      <ns2:TransferBetweenAccountsResponse xmlns:ns2=\"T24WebServicesImpl\">" +
                            "         <Status>" +
                            "            <transactionId>" + generateTransactionId() + "</transactionId>" +
                            "            <messageId>" + generateFormattedReferenceNo() + "</messageId>" +
                            "            <successIndicator>T24Error</successIndicator>" +
                            "            <application>NO</application>" +
                            "            <messages>USERNAME / PASSWORD SALAH</messages>" +
                            "         </Status>" +
                            "      </ns2:TransferBetweenAccountsResponse>" +
                            "   </S:Body>" +
                            "</S:Envelope>";
            return ResponseEntity.status(200).body(soapResponse);
        }

        // Validate DebitAccount
        if (!isValidDebitAccount(debitAccount)) {
            String soapResponse =
                    "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "   <S:Body>" +
                            "      <ns2:TransferBetweenAccountsResponse xmlns:ns2=\"T24WebServicesImpl\">" +
                            "         <Status>" +
                            "            <transactionId>" + generateTransactionId() + "</transactionId>" +
                            "            <messageId>" + generateFormattedReferenceNo() + "</messageId>" +
                            "            <successIndicator>T24Error</successIndicator>" +
                            "            <application>NO</application>" +
                            "            <messages>SOURCE ACCOUNT NOT FOUND</messages>" +
                            "         </Status>" +
                            "      </ns2:TransferBetweenAccountsResponse>" +
                            "   </S:Body>" +
                            "</S:Envelope>";
            return ResponseEntity.badRequest().body(soapResponse); // 400 Bad Request
        }

        // Validate DebitAmount
        int debitAmount = Integer.parseInt(debitAmountStr);
        if (debitAmount < MIN_DEBIT_AMOUNT) {
            String soapResponse =
                    "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "   <S:Body>" +
                            "      <ns2:TransferBetweenAccountsResponse xmlns:ns2=\"T24WebServicesImpl\">" +
                            "         <Status>" +
                            "            <transactionId>" + generateTransactionId() + "</transactionId>" +
                            "            <messageId>" + generateFormattedReferenceNo() + "</messageId>" +
                            "            <successIndicator>T24Error</successIndicator>" +
                            "            <application>NO</application>" +
                            "            <messages>MINIMUM TRANSAKSI Rp.10.000,00</messages>" +
                            "         </Status>" +
                            "      </ns2:TransferBetweenAccountsResponse>" +
                            "   </S:Body>" +
                            "</S:Envelope>";
            return ResponseEntity.badRequest().body(soapResponse); // 400 Bad Request
        }

        // Validate CreditAccount
        if (!isValidCreditAccount(creditAccount)) {
            String soapResponse =
                    "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "   <S:Body>" +
                            "      <ns2:TransferBetweenAccountsResponse xmlns:ns2=\"T24WebServicesImpl\">" +
                            "         <Status>" +
                            "            <transactionId>" + generateTransactionId() + "</transactionId>" +
                            "            <messageId>" + generateFormattedReferenceNo() + "</messageId>" +
                            "            <successIndicator>T24Error</successIndicator>" +
                            "            <application>NO</application>" +
                            "            <messages>DESTINATION ACCOUNT NOT FOUND</messages>" +
                            "         </Status>" +
                            "      </ns2:TransferBetweenAccountsResponse>" +
                            "   </S:Body>" +
                            "</S:Envelope>";
            return ResponseEntity.badRequest().body(soapResponse); // 400 Bad Request
        }

        // Static SOAP XML Response with dynamic transactionId and messageId
        String soapResponse =
                "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "   <S:Body>" +
                        "      <ns2:TransferBetweenAccountsResponse xmlns:ns2=\"T24WebServicesImpl\">" +
                        "         <Status>" +
                        "            <transactionId>" + generateTransactionId() + "</transactionId>" +
                        "            <messageId>" + generateFormattedReferenceNo() + "</messageId>" +
                        "            <successIndicator>Success</successIndicator>" +
                        "            <application>FUNDS.TRANSFER</application>" +
                        "         </Status>" +
                        "         <FUNDSTRANSFERType id=\"" + generateTransactionId() + "\">" +
                        "            <COCODE>ID0010001</COCODE>" +
                        "            <MSGID>" + generateFormattedReferenceNo() + "</MSGID>" +
                        "         </FUNDSTRANSFERType>" +
                        "      </ns2:TransferBetweenAccountsResponse>" +
                        "   </S:Body>" +
                        "</S:Envelope>";

        return ResponseEntity.ok(soapResponse);
    }
    private String extractFieldFromRequest(String request, String fieldName) {
        // Simple extraction logic; you might want to use a more robust XML parser
        String tagOpen = "<" + fieldName + ">";
        String tagClose = "</" + fieldName + ">";
        int startIndex = request.indexOf(tagOpen);
        int endIndex = request.indexOf(tagClose);
        if (startIndex != -1 && endIndex != -1) {
            startIndex += tagOpen.length();
            return request.substring(startIndex, endIndex);
        }
        return "";
    }
    private boolean isValidDebitAccount(String debitAccount) {
        for (String validAccount : VALID_DEBIT_ACCOUNTS) {
            if (validAccount.equals(debitAccount)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCreditAccount(String creditAccount) {
        for (String validAccount : VALID_CREDIT_ACCOUNTS) {
            if (validAccount.equals(creditAccount)) {
                return true;
            }
        }
        return false;
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
    private String generateFormattedReferenceNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

}