package com.research.randy.model;

public class transferIntraBankRequest {
    private String partnerReferenceNo;
    public Amount amount;
    private String beneficiaryAccountNo;
    private String sourceAccountNo;
    private String remark;
    private AdditionalInfo additionalInfo;  // Mengubah Map menjadi class AdditionalInfo

    // Getters and Setters


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getPartnerReferenceNo() {
        return partnerReferenceNo;
    }

    public void setPartnerReferenceNo(String partnerReferenceNo) {
        this.partnerReferenceNo = partnerReferenceNo;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getBeneficiaryAccountNo() {
        return beneficiaryAccountNo;
    }

    public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
        this.beneficiaryAccountNo = beneficiaryAccountNo;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
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

    // Kelas baru AdditionalInfo
    public static class AdditionalInfo {
        private String deviceId;
        private String channel;

        // Getters and Setters

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

}
