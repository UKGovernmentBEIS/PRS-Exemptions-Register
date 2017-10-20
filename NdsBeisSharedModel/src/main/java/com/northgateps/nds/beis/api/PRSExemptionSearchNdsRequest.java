package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to get exemptions from Beis Back office, submitted as a request to the ESB.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRSExemptionSearchNdsRequest", propOrder = { "exemptionSearch"})
@XmlRootElement(name = "PRSExemptionSearchNdsRequest")
public class PRSExemptionSearchNdsRequest implements NdsRequest {
    
    protected ExemptionSearch exemptionSearch;
    
    
    public ExemptionSearch getExemptionSearch() {
        return exemptionSearch;
    }

    
    public void setExemptionSearch(ExemptionSearch exemptionSearch) {
        this.exemptionSearch = exemptionSearch;
    }

}
