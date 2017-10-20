package com.northgateps.nds.beis.api.getreferencevalues;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * The response from the ESB detailing the outcome of the application for BEIS
 * Unsuccessful response will be detailed in the NdsMessages
 * Successful response will be detailed in the Success
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReferenceValuesNdsResponse", propOrder = { "getReferenceValuesResponseDetails" })
@XmlRootElement(name = "GetReferenceValuesNdsResponse")
public class GetReferenceValuesNdsResponse extends AbstractNdsResponse {

    @XmlElement(name = "GetReferenceValuesResponseDetails")
    protected GetReferenceValuesResponseDetails getReferenceValuesResponseDetails;

    public GetReferenceValuesResponseDetails getGetReferenceValuesResponseDetails() {
        return getReferenceValuesResponseDetails;
    }

    public void setGetReferenceValuesResponseDetails(
            GetReferenceValuesResponseDetails getReferenceValuesResponseDetails) {
        this.getReferenceValuesResponseDetails = getReferenceValuesResponseDetails;
    }
}
