package com.northgateps.nds.beis.api;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Class to contain exemption information search result
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionData", propOrder = { "exemptionRefNo", "pwsDescription",
        "registeredDate", "landLordName", "address", "epcExists", "epcObject", "mimeType", 
        "service", "exemptionReasonCode", "epcBand", "epcRating" })
public class ExemptionData implements Serializable {

    private String exemptionRefNo;
    private String service;
    private String exemptionReasonCode;
    private String pwsDescription;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime registeredDate;

    private String landLordName;
    private String address;
    private String epcBand;
    private String epcRating;

    private boolean epcExists;
    
    private byte[] epcObject;
    
    private String mimeType;

    public byte[] getEpcObject() {
        return epcObject;
    }

    
    public void setEpcObject(byte[] epcObject) {
        this.epcObject = epcObject;
    }

    
    public boolean isEpcExists() {
        return epcExists;
    }


    
    public void setEpcExists(boolean epcExists) {
        this.epcExists = epcExists;
    }


    public String getExemptionRefNo() {
        return exemptionRefNo;
    }

    public void setExemptionRefNo(String exemptionRefNo) {
        this.exemptionRefNo = exemptionRefNo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getExemptionReasonCode() {
        return exemptionReasonCode;
    }

    public void setExemptionReasonCode(String exemptionReasonCode) {
        this.exemptionReasonCode = exemptionReasonCode;
    }

    public String getPwsDescription() {
        return pwsDescription;
    }

    public void setPwsDescription(String pwsDescription) {
        this.pwsDescription = pwsDescription;
    }

    public ZonedDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(ZonedDateTime registeredDate) {
        this.registeredDate = registeredDate;
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

    public String getEpcBand() {
        return epcBand;
    }

    public void setEpcBand(String epcBand) {
        this.epcBand = epcBand;
    }

    public String getEpcRating() {
        return epcRating;
    }

    public void setEpcRating(String epcRating) {
        this.epcRating = epcRating;
    }
    
    public String getMimeType() {
        return mimeType;
    }


    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}
