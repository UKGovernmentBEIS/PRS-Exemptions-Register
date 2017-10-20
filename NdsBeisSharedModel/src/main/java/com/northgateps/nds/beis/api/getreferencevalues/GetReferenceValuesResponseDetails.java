package com.northgateps.nds.beis.api.getreferencevalues;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * Class to contain all the information for a Get Reference Values Details response , that is returned from the server
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReferenceValuesResponseDetails", propOrder = { "referenceValuesDetails" })
public class GetReferenceValuesResponseDetails extends AbstractValidatableModel {
    
    @XmlElement(name = "ReferenceValuesDetails", required = true)
    private List<ReferenceValuesDetails> referenceValuesDetails;

    public List<ReferenceValuesDetails> getReferenceValuesDetails() {
        return referenceValuesDetails;
    }

    public void setReferenceValuesDetails(List<ReferenceValuesDetails> referenceValuesDetails) {
        this.referenceValuesDetails = referenceValuesDetails;
    }
}