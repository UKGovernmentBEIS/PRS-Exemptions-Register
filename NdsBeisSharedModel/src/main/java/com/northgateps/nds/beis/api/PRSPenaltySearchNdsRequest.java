package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to get penalties from Beis Back office, submitted as a request to the ESB.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRSPenaltySearchNdsRequest", propOrder = { "penaltySearch"})
@XmlRootElement(name = "PRSPenaltySearchNdsRequest")
public class PRSPenaltySearchNdsRequest implements NdsRequest {
    
    protected PenaltySearch penaltySearch;

    
    public PenaltySearch getPenaltySearch() {
        return penaltySearch;
    }

    
    public void setPenaltySearch(PenaltySearch penaltySearch) {
        this.penaltySearch = penaltySearch;
    }

}
