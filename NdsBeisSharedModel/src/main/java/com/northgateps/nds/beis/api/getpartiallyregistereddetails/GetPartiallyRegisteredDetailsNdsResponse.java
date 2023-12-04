package com.northgateps.nds.beis.api.getpartiallyregistereddetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.platform.api.NdsErrorResponse;

/**
 * This class will return the partially registered details
 * for the user.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPartiallyRegisteredDetailsNdsResponse", propOrder = {"partiallyRegisteredDetails"})
@XmlRootElement(name = "GetPartiallyRegisteredDetailsNdsResponse")
public class GetPartiallyRegisteredDetailsNdsResponse extends NdsErrorResponse{
    
    protected UpdateBeisRegistrationDetails partiallyRegisteredDetails;
    
    public UpdateBeisRegistrationDetails getPartiallyRegisteredDetails(){
        return partiallyRegisteredDetails;
    }
    
    public void setPartiallyRegisteredDetails(UpdateBeisRegistrationDetails partiallyRegisteredDetails){
        this.partiallyRegisteredDetails = partiallyRegisteredDetails;
    }
}
