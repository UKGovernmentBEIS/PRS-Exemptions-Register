package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrganisationNameDetail", propOrder = { "orgName" })
public class OrganisationNameDetail extends AbstractValidatableModel {

    @RequiredFieldMetadata
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
