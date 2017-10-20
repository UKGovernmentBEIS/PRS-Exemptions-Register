package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.validation.js.BeisAddressConsolidationWithCountryJoiner;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
/***
 * 
 * Store registration details
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeisRegistrationDetails", propOrder = { "accountDetails", "contactAddress", "userDetails", "tenant",
        "activationCode" })
public class BeisRegistrationDetails extends AbstractValidatableModel {

    @RequiredFieldMetadata
    private BeisAccountDetails accountDetails;

    @RequiredFieldMetadata
    @GenericFieldMetadata(joiner = BeisAddressConsolidationWithCountryJoiner.class)
    private BeisAddressDetail contactAddress;

    @RequiredFieldMetadata
    private BeisUserDetails userDetails;

    @RequiredFieldMetadata
    private String tenant;

    private String activationCode;

    public BeisAccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(BeisAccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public BeisAddressDetail getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(BeisAddressDetail contactAddress) {
        this.contactAddress = contactAddress;
    }

    public BeisUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(BeisUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

}
