package com.research.randy.model;

import java.util.List;

public class transferIntraBankApiRequest {

    private String partner_reference_no;
    private Amount amount;
    private String beneficiary_account_no;
    private String source_account_no;

    public List<String> getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(List<String> transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    private List<String> transactionDetail;
    private AdditionalInfo additional_info;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    private String httpStatus;

    // Getters and Setters

    public String getPartner_reference_no() {
        return partner_reference_no;
    }

    public void setPartner_reference_no(String partner_reference_no) {
        this.partner_reference_no = partner_reference_no;
    }

    public String getSource_account_no() {
        return source_account_no;
    }

    public void setSource_account_no(String source_account_no) {
        this.source_account_no = source_account_no;
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
