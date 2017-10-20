package com.northgateps.nds.beis.api.beisregistration;

import javax.xml.bind.annotation.XmlAccessType;
/**
 * The NDS request to register a new user 
 */
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to register a new user
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeisRegistrationNdsRequest", propOrder = { "registrationDetails" })
@XmlRootElement(name = "BeisRegistrationNdsRequest")
public class BeisRegistrationNdsRequest implements NdsRequest {

    protected BeisRegistrationDetails registrationDetails;

    public BeisRegistrationDetails getRegistrationDetails() {
        return registrationDetails;
    }

    public void setRegistrationDetails(BeisRegistrationDetails registrationDetails) {
        this.registrationDetails = registrationDetails;
    }

}
