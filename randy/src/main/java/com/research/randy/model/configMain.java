package com.research.randy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "RANDY_CFG_MAIN")
public class configMain {
    @Id
    @Column(name = "KEY") // Periksa nama kolom
    private String keyParam;
    @Column(name = "KEYGROUP") // Periksa nama kolom
    private String keyGroup;
    @Column(name = "VALUE") // Periksa nama kolom
    private String value;
    @Column(name = "ISENABLE") // Periksa nama kolom
    private String isenable;

    // Getters and Setters
    public String getKeyParam() {
        return keyParam;
    }
    public void setKeyParam(String keyParam) {
        this.keyParam = keyParam;
    }
    public String getKeyGroup() {
        return keyGroup;
    }
    public void setKeyGroup(String keyGroup) {
        this.keyGroup = keyGroup;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getisenable() {
        return isenable;
    }
    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

}
