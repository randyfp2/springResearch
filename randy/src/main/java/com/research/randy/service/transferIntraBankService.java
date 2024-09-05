package com.research.randy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.research.randy.cache.getConfigMainService;
import com.research.randy.config.RestTemplateConfig;
import com.research.randy.mappingService.transferIntraBankMappingService;
import com.research.randy.model.transferIntraBankApiRequest;
import com.research.randy.model.transferIntraBankApiResponse;
import com.research.randy.model.transferIntraBankRequest;
import com.research.randy.model.transferIntraBankResponse;
import com.research.randy.util.encryptDecryptAES256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class transferIntraBankService {

    private final RestTemplate restTemplate;
    private final RestTemplateConfig coreBankRestTemplate;
    private final ObjectMapper objectMapper;
    private final transferIntraBankMappingService mappingService;
    // Variabel untuk menyimpan request dan response eksternal
    private String apiExternalRequest;
    private String apiExternalResponse;
    private String apiExternalHttpStatus;
    private String apiExternalURL;

    private getConfigMainService parameterService;

    @Value("${app.global.coreBanking.URL}")
    private String coreBankURL;
    @Value("${app.global.coreBanking.userName}")
    private String coreBankUsername;
    @Value("${app.global.coreBanking.password}")
    private String coreBankPassword;
    @Value("${app.global.coreBanking.passwordEncrypt}")
    private String coreBankPasswordEncrypt;

    @Autowired
    private encryptDecryptAES256 aesEncryptionService;

    /* menggunakan global variable
    @Value("${app.global.coreBanking.URL}")
    private String coreBankURL;
    @Value("${app.global.coreBanking.userName}")
    private String coreBankUsername;
    @Value("${app.global.coreBanking.password}")
    private String coreBankPassword;
     */

    @Autowired
    public transferIntraBankService(@Qualifier("coreBankRestTemplate")RestTemplate restTemplate, RestTemplateConfig coreBankRestTemplate, transferIntraBankMappingService mappingService,getConfigMainService parameterService) {
        this.restTemplate = restTemplate;
        this.coreBankRestTemplate = coreBankRestTemplate;
        this.mappingService = mappingService;
        this.parameterService = parameterService;
        //obbjectMapper untuk convert otomatis ke snake_case
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    }

    public transferIntraBankResponse transfer(transferIntraBankRequest transferRequest) throws Exception {
        try {
            // Convert camelCase request to snake_case JSON
            // String jsonBody = objectMapper.writeValueAsString(transferRequest);
            // Manual mapping from camelCase to snake_case
            transferIntraBankApiRequest snakeCaseRequest = new transferIntraBankApiRequest();
            snakeCaseRequest.setPartner_reference_no(transferRequest.getPartnerReferenceNo());
            // Handle amount field
            transferIntraBankApiRequest.Amount amount = new transferIntraBankApiRequest.Amount();
            amount.setValue(transferRequest.getAmount().getValue());
            amount.setCurrency(transferRequest.getAmount().getCurrency());
            snakeCaseRequest.setAmount(amount);
            // akhir handle amount
            snakeCaseRequest.setBeneficiary_account_no(transferRequest.getBeneficiaryAccountNo());
            snakeCaseRequest.setSource_account_no(transferRequest.getSourceAccountNo());
            List<String> transactionDetails = splitRemarkIntoChunks(transferRequest.getRemark(), 10);
            snakeCaseRequest.setTransactionDetail(transactionDetails);

            // Mapping AdditionalInfo if available
            if (transferRequest.getAdditionalInfo() != null) {
                transferIntraBankApiRequest.AdditionalInfo additionalInfo = new transferIntraBankApiRequest.AdditionalInfo();
                additionalInfo.setDevice_id(transferRequest.getAdditionalInfo().getDeviceId());
                additionalInfo.setChannel(transferRequest.getAdditionalInfo().getChannel());
                snakeCaseRequest.setAdditional_info(additionalInfo);
            }

            // Convert the snake_case object to JSON
            String jsonBodyRequest = new ObjectMapper().writeValueAsString(snakeCaseRequest);
            // System.out.println("transferIntraBank API Request: " + jsonBodyRequest);

            // Store the API external request
           this.apiExternalRequest = jsonBodyRequest;
            /*
            String coreBankURL = parameterService.getParameterValue("TWS_ENDPOINT_URL");
            String coreBankUsername = parameterService.getParameterValue("TWS_AUTH_USER");
            String coreBankPassword = parameterService.getParameterValue("TWS_AUTH_PASSWORD");

            System.out.println("url: " + coreBankURL);
            System.out.println("uname: " + coreBankUsername);
            System.out.println("pwd: " + coreBankPassword);

             */


            // Generate AES256 key
            // Data yang akan dienkripsi
            /*
            String originalData = "manage";
            // Enkripsi data
            String encryptedData = aesEncryptionService.encrypt(originalData);
            System.out.println("Encrypted Data: " + encryptedData);
             */
            String decryptedPassword = aesEncryptionService.decrypt(coreBankPasswordEncrypt);
            // System.out.println("Decrypted Data: " + decryptedPassword);
            // Prepare headers for Basic Auth ibarat mapping dokumen ke header di IS
            HttpHeaders headers = new HttpHeaders();
            String apiExternalUrl = coreBankURL;
            headers.setBasicAuth(coreBankUsername, coreBankPassword);
            /* tesst cetak
            System.out.println("url: " + apiExternalUrl);
            System.out.println("uname: " + coreBankUsername);
            System.out.println("pwd: " + coreBankPassword);
             */
            headers.add("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBodyRequest, headers);
            this.apiExternalURL = apiExternalUrl;
            // Send request to the external API and get the response
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiExternalUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Capture HTTP status from the response
            HttpStatus httpStatusCode = (HttpStatus) responseEntity.getStatusCode();
            // System.out.println("HTTP Status transferIntraBank API: " + httpStatusCode);
            // Capture HTTP status from the response dan map untuk logging
            this.apiExternalHttpStatus = responseEntity.getStatusCode().toString();

            // Convert response body from JSON to TransferIntraBankApiResponse
            transferIntraBankApiResponse apiResponse = objectMapper.readValue(responseEntity.getBody(), transferIntraBankApiResponse.class);
            // Set HTTP status in the API response object
            apiResponse.setHttpStatus(String.valueOf(httpStatusCode));

            // System.out.println("transferIntraBank API Response: " + responseEntity.getBody());
            // Store the API external response
            this.apiExternalResponse = responseEntity.getBody();
            // Map API response to camelCase response
            return mappingService.mapToCamelCase(apiResponse, transferRequest);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            // Tangani exception spesifik HTTP errors dan timeout

            transferIntraBankResponse errorResponse = new transferIntraBankResponse();

            if (e instanceof ResourceAccessException) {
                // Tangani timeout khusus
                errorResponse.setHttpStatus("408"); // HTTP 408 Request Timeout
                errorResponse.setResponseMessage("Request timeout occurred");
            } else {
                // Tangani HTTP errors lainnya
                errorResponse.setHttpStatus(((HttpStatusCodeException) e).getStatusCode().toString());
                errorResponse.setResponseMessage(((HttpStatusCodeException) e).getResponseBodyAsString());
            }

            return errorResponse;
        } catch (IOException e) {
            // Tangani exception terkait IO, seperti kesalahan saat konversi JSON
            System.out.println("IO Error: " + e.getMessage());
            throw e; // Rethrow exception jika perlu
        }
        catch (Exception e) {
            // Tangani exception umum lainnya
            System.out.println("Unexpected Error: " + e.getMessage());
            throw e; // Rethrow exception jika perlu
        }
    }

    // Getters for API external request, response, and HTTP status
    public String getApiExternalRequest() {
        return apiExternalRequest;
    }
    public String getApiExternalResponse() {
        return apiExternalResponse;
    }
    public String getApiExternalHttpStatus() {
        return apiExternalHttpStatus;
    }
    public String getApiExternalUrl() {
        return apiExternalURL;
    }
    private List<String> splitRemarkIntoChunks(String remark, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < remark.length(); i += chunkSize) {
            chunks.add(remark.substring(i, Math.min(remark.length(), i + chunkSize)));
        }
        return chunks;
    }
}
