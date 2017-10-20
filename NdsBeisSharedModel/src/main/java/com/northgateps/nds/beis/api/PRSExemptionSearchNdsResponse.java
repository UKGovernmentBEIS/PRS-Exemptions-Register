package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * The response from the ESB detailing the Exemption search result
 * Unsuccessful response will be detailed in the NdsMessages
 * Successful response will be detailed in the Success
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRSExemptionSearchNdsResponse", propOrder = { "getExemptionSearchResponseDetail" })
@XmlRootElement(name = "PRSExemptionSearchNdsResponse")
public class PRSExemptionSearchNdsResponse extends AbstractNdsResponse {

    private GetExemptionSearchResponseDetail getExemptionSearchResponseDetail;

    public GetExemptionSearchResponseDetail getGetExemptionSearchResponseDetail() {
        return getExemptionSearchResponseDetail;
    }

    public void setGetExemptionSearchResponseDetail(GetExemptionSearchResponseDetail getExemptionSearchResponseDetail) {
        this.getExemptionSearchResponseDetail = getExemptionSearchResponseDetail;
    }
}
