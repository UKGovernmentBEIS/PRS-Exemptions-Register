package com.northgateps.nds.beis.api;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Class to contain all the information for a Exemptions response , that is returned from the server
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetExemptionList", propOrder = { "exemptions" })
public class GetExemptionSearchResponseDetail implements Serializable {

    private List<ExemptionData> exemptions;

    public List<ExemptionData> getExemptions() {
        return exemptions;
    }

    public void setExemptions(List<ExemptionData> exemptions) {
        this.exemptions = exemptions;
    }

}
