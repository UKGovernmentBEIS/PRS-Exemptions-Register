package com.northgateps.nds.beis.api.getreferencevalues;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Retrieving updated values of various name-value pairs (Master List) reference values
 * which are maintained in back office.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReferenceValuesNdsRequest", propOrder = { "referenceValuesDetails" })
@XmlRootElement(name = "GetReferenceValuesNdsRequest")
public class GetReferenceValuesNdsRequest implements NdsRequest {

    protected ReferenceValuesDetails referenceValuesDetails;

    public ReferenceValuesDetails getReferenceValuesDetails() {
        return referenceValuesDetails;
    }

    public void setReferenceValuesDetails(ReferenceValuesDetails referenceValuesDetails) {
        this.referenceValuesDetails = referenceValuesDetails;
    }
}