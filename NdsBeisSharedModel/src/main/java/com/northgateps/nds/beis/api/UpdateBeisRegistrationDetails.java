package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * Stores the updatable registration details
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateBeisRegistrationDetails", propOrder = { "accountDetails", "contactAddress", "updateUserDetails" })
public class UpdateBeisRegistrationDetails extends AbstractValidatableModel {

	BeisAccountDetails accountDetails;
	
	BeisAddressDetail contactAddress;
	
	BeisUpdateUserDetails updateUserDetails;
		
	public BeisAccountDetails getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(BeisAccountDetails accountDetails) {
		this.accountDetails = accountDetails;
	}
	
	public BeisAddressDetail getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(BeisAddressDetail contactAddress) {
		this.contactAddress = contactAddress;
	}
	
	public BeisUpdateUserDetails getUpdateUserDetails(){
	    return updateUserDetails;
	}
	
	public void setUpdateUserDetails(BeisUpdateUserDetails updateUserDetails){
	    this.updateUserDetails = updateUserDetails;
	}
}
