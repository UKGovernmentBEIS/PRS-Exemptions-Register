package com.northgateps.nds.beis.api.retrieveregistereddetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.platform.api.NdsErrorResponse;

/**
 * Response to retrieving registered user details of a user
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveRegisteredDetailsNdsResponse", propOrder = {"beisRegistrationDetails"})
@XmlRootElement(name = "RetrieveRegisteredDetailsNdsResponse")
public class RetrieveRegisteredDetailsNdsResponse extends NdsErrorResponse {
    
	protected BeisRegistrationDetails beisRegistrationDetails;
	
	public BeisRegistrationDetails getBeisRegistrationDetails() {
	    return beisRegistrationDetails;
	}
	
	
	public void setBeisRegistrationDetails(BeisRegistrationDetails beisRegistrationDetails) {
	    this.beisRegistrationDetails = beisRegistrationDetails;
	}
}