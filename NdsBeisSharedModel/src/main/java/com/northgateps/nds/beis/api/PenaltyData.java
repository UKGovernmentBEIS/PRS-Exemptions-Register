package com.northgateps.nds.beis.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Class to contain penalty information search result
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PenaltyData", propOrder = { "penaltyRefNo", "service", 
        "penaltyReasonCode", "pwsDescription", "amount","landLordName", 
        "address" })
public class PenaltyData implements Serializable {

    private BigInteger penaltyRefNo;
    private String service;
    private String penaltyReasonCode;
    private String pwsDescription;
    private BigDecimal amount;
    private String landLordName;
    private String address;

    public BigInteger getPenaltyRefNo() {
        return penaltyRefNo;
    }

    public void setPenaltyRefNo(BigInteger penaltyRefNo) {
        this.penaltyRefNo = penaltyRefNo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPenaltyReasonCode() {
        return penaltyReasonCode;
    }

    public void setPenaltyReasonCode(String penaltyReasonCode) {
        this.penaltyReasonCode = penaltyReasonCode;
    }

    public String getPwsDescription() {
        return pwsDescription;
    }

    public void setPwsDescription(String pwsDescription) {
        this.pwsDescription = pwsDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLandLordName() {
        return landLordName;
    }

    public void setLandLordName(String landLordName) {
        this.landLordName = landLordName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
