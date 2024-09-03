package com.research.randy.model;

import org.springframework.http.HttpStatus;

public class transferIntraBankApiResponse {
    private String response_code;
    private String response_message;
    private String error_code;
    private String error_message;
    private String error_cat;
    private String reference_no;
    private String partner_reference_no;
    private String transaction_id;
    private Amount amount;
    private String beneficiary_account_no;
    private String beneficiary_bank_code;
    private String source_account_no;
    private String receipt_number;
    private String customer_reference;
    private String network_route;
    private AdditionalInfo additional_info;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    private String httpStatus;

    // Getters and Setters



    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError_cat() {
        return error_cat;
    }

    public void setError_cat(String error_cat) {
        this.error_cat = error_cat;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getPartner_reference_no() {
        return partner_reference_no;
    }

    public void setPartner_reference_no(String partner_reference_no) {
        this.partner_reference_no = partner_reference_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getBeneficiary_account_no() {
        return beneficiary_account_no;
    }

    public void setBeneficiary_account_no(String beneficiary_account_no) {
        this.beneficiary_account_no = beneficiary_account_no;
    }

    public String getBeneficiary_bank_code() {
        return beneficiary_bank_code;
    }

    public void setBeneficiary_bank_code(String beneficiary_bank_code) {
        this.beneficiary_bank_code = beneficiary_bank_code;
    }

    public String getSource_account_no() {
        return source_account_no;
    }

    public void setSource_account_no(String source_account_no) {
        this.source_account_no = source_account_no;
    }

    public String getReceipt_number() {
        return receipt_number;
    }

    public void setReceipt_number(String receipt_number) {
        this.receipt_number = receipt_number;
    }

    public String getCustomer_reference() {
        return customer_reference;
    }

    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getNetwork_route() {
        return network_route;
    }

    public void setNetwork_route(String network_route) {
        this.network_route = network_route;
    }

    public AdditionalInfo getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(AdditionalInfo additional_info) {
        this.additional_info = additional_info;
    }

    public static class Amount {
        private String value;
        private String currency;

        // Getters and Setters

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class AdditionalInfo {
        private String device_id;
        private String channel;

        // Getters and Setters

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
