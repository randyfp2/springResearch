package com.research.randy.model.ISO8583;

import java.util.Map;

public class ISO8583Response {
    private Map<String, String> isoRes;

    public Map<String, String> getIsoRes() {
        return isoRes;
    }

    public void setIsoRes(Map<String, String> isoRes) {
        this.isoRes = isoRes;
    }

    @Override
    public String toString() {
        return "ISO8583Response{" +
                "isoRes=" + isoRes +
                '}';
    }
}