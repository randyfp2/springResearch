package com.research.randy.model.logging;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RANDY_RESEARCH_LOG")
public class LoggingModel {

    @Column(name = "SERVICEURL")
    private String serviceUrl;

    @Column(name = "PARTNERREFERENCENO")
    private String partnerReferenceNo;

    @Column(name = "RESPONSECODE")
    private String responseCode;

    @Column(name = "RESPONSEMESSAGE")
    private String responseMessage;

    @Column(name = "INCOMINGREQUESTTIME")
    private String incomingRequestTime;

    @Column(name = "INCOMINGREQUEST")
    private String incomingRequest;

    @Column(name = "APIEXTERNALURL")
    private String apiExternalURL;

    @Column(name = "APIEXTERNALREQUEST")
    private String apiExternalRequest;

    @Column(name = "APIEXTERNALRESPONSE")
    private String apiExternalResponse;

    @Column(name = "APIEXTERNALHTTPSTATUS")
    private String apiExternalHttpStatus;

    @Column(name = "INCOMINGRESPONSE")
    private String incomingResponse;

    @Column(name = "INCOMINGRESPONSETIME")
    private String incomingResponseTime;

    @Column(name = "LOCALIP")
    private String localIp;
    @Id
    @Column(name = "REFERENCENO")
    private String referenceNo;


    // Getters and Setters
    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getPartnerReferenceNo() {
        return partnerReferenceNo;
    }

    public void setPartnerReferenceNo(String partnerReferenceNo) {
        this.partnerReferenceNo = partnerReferenceNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getIncomingRequestTime() {
        return incomingRequestTime;
    }

    public void setIncomingRequestTime(String incomingRequestTime) {
        this.incomingRequestTime = incomingRequestTime;
    }

    public String getIncomingRequest() {
        return incomingRequest;
    }

    public void setIncomingRequest(String incomingRequest) {
        this.incomingRequest = incomingRequest;
    }

    public String getApiExternalURL() {
        return apiExternalURL;
    }

    public void setApiExternalURL(String apiExternalURL) {
        this.apiExternalURL = apiExternalURL;
    }

    public String getApiExternalRequest() {
        return apiExternalRequest;
    }

    public void setApiExternalRequest(String apiExternalRequest) {
        this.apiExternalRequest = apiExternalRequest;
    }

    public String getApiExternalResponse() {
        return apiExternalResponse;
    }

    public void setApiExternalResponse(String apiExternalResponse) {
        this.apiExternalResponse = apiExternalResponse;
    }

    public String getApiExternalHttpStatus() {
        return apiExternalHttpStatus;
    }

    public void setApiExternalHttpStatus(String apiExternalHttpStatus) {
        this.apiExternalHttpStatus = apiExternalHttpStatus;
    }
    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    public String getIncomingResponse() {
        return incomingResponse;
    }

    public void setIncomingResponse(String incomingResponse) {
        this.incomingResponse = incomingResponse;
    }

    public String getIncomingResponseTime() {
        return incomingResponseTime;
    }

    public void setIncomingResponseTime(String incomingResponseTime) {
        this.incomingResponseTime = incomingResponseTime;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }


}
