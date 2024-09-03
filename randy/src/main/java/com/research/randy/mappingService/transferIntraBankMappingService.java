package com.research.randy.mappingService;
import com.research.randy.model.transferIntraBankRequest;
import com.research.randy.model.transferIntraBankResponse;
import com.research.randy.model.transferIntraBankApiResponse;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class transferIntraBankMappingService {
    private static final Logger logger = LoggerFactory.getLogger(transferIntraBankMappingService.class);
    public transferIntraBankResponse mapToCamelCase(transferIntraBankApiResponse response, transferIntraBankRequest request) {
        transferIntraBankResponse camelCaseResponse = new transferIntraBankResponse();
        // Map fields from snake_case to camelCase
        camelCaseResponse.setResponseCode(response.getResponse_code());
        camelCaseResponse.setResponseMessage(response.getResponse_message());
        camelCaseResponse.setErrorCode(response.getError_code());
        camelCaseResponse.setErrorMessage(response.getError_message());
        camelCaseResponse.setErrorCat(response.getError_cat());
        camelCaseResponse.setReferenceNo(response.getReference_no());
        camelCaseResponse.setPartnerReferenceNo(response.getPartner_reference_no());
        camelCaseResponse.setTransactionId(response.getTransaction_id()); // Generate new transaction ID
        // camelCaseResponse.setAmount(response.getAmount());
        // Log the request amount
        // logger.info("Request amount: {}", request.getAmount());
        // Handle amount field
        transferIntraBankResponse.Amount amount = new transferIntraBankResponse.Amount();
        amount.setValue(request.getAmount().getValue());
        amount.setCurrency(request.getAmount().getCurrency());
        camelCaseResponse.setAmount(amount);


        camelCaseResponse.setBeneficiaryAccountNo(response.getBeneficiary_account_no());
        camelCaseResponse.setBeneficiaryBankCode(response.getBeneficiary_bank_code());
        camelCaseResponse.setSourceAccountNo(response.getSource_account_no());
        camelCaseResponse.setReceiptNumber(response.getReceipt_number());
        camelCaseResponse.setCustomerReference(response.getCustomer_reference());
        camelCaseResponse.setNetworkRoute(response.getNetwork_route());

        // Handle additional info
        transferIntraBankResponse.AdditionalInfo additionalInfo = new transferIntraBankResponse.AdditionalInfo();
        additionalInfo.setDeviceId(request.getAdditionalInfo().getDeviceId());
        additionalInfo.setChannel(request.getAdditionalInfo().getChannel());
        camelCaseResponse.setAdditionalInfo(additionalInfo);

        //simpanHTTPStatus
        camelCaseResponse.setHttpStatus(response.getHttpStatus());

        return camelCaseResponse;

    }
}