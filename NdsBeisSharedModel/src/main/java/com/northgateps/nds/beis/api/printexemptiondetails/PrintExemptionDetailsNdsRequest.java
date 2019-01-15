package com.northgateps.nds.beis.api.printexemptiondetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.platform.api.NdsRequest;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;

/**
 * Request to retrieve a PDF containing details of an exemption.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrintExemptionDetailsNdsRequest", propOrder = { "tenant", "exemptionDetail", "exemptionType", "propertyType" })
@XmlRootElement(name = "PrintExemptionDetailsNdsRequest")
public class PrintExemptionDetailsNdsRequest implements NdsRequest {

    @RequiredFieldMetadata
    private String tenant;

    @RequiredFieldMetadata
    private ExemptionDetail exemptionDetail;

    @RequiredFieldMetadata
    private ExemptionTypeDetails exemptionType;
    
    private String propertyType;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public ExemptionDetail getExemptionDetail() {
        return exemptionDetail;
    }

    public void setExemptionDetail(ExemptionDetail exemptionDetail) {
        this.exemptionDetail = exemptionDetail;
    }

    public ExemptionTypeDetails getExemptionType() {
        return exemptionType;
    }

    public void setExemptionType(ExemptionTypeDetails exemptionType) {
        this.exemptionType = exemptionType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
}
