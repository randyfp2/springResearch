package com.research.randy.model.ISO8583;

import java.util.Map;

public class ISO8583Request {
    private Map<String, String> isoReq;

    // Getters and setters
    public Map<String, String> getIsoReq() {
        return isoReq;
    }

    public void setIsoReq(Map<String, String> isoReq) {
        this.isoReq = isoReq;
    }
}
