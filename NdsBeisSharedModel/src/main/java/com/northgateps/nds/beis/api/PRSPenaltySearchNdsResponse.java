package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * The response from the ESB detailing the Penalty search result
 * Unsuccessful response will be detailed in the NdsMessages
 * Successful response will be detailed in the Success
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRSPenaltySearchNdsResponse", propOrder = { "getPenaltySearchResponseDetail" })
@XmlRootElement(name = "PRSPenaltySearchNdsResponse")
public class PRSPenaltySearchNdsResponse extends AbstractNdsResponse {

    private GetPenaltySearchResponseDetail getPenaltySearchResponseDetail;

    public GetPenaltySearchResponseDetail getGetPenaltySearchResponseDetail() {
        return getPenaltySearchResponseDetail;
    }

    public void setGetPenaltySearchResponseDetail(GetPenaltySearchResponseDetail getPenaltySearchResponseDetail) {
        this.getPenaltySearchResponseDetail = getPenaltySearchResponseDetail;
    }

}
