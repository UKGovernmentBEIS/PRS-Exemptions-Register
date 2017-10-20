package com.northgateps.nds.beis.api.getexemptiontypetext;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * Response to hold exemption type text from the back office
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetExemptionTypeTextNdsResponse", propOrder = { "exemptionTypeTextList" })
@XmlRootElement(name = "GetExemptionTypeTextNdsResponse")
public class GetExemptionTypeTextNdsResponse extends AbstractNdsResponse {

    List<ExemptionTypeDetails> exemptionTypeTextList;

    public List<ExemptionTypeDetails> getExemptionTypeTextList() {
        return exemptionTypeTextList;
    }

    public void setExemptionTypeTextList(List<ExemptionTypeDetails> exemptionTypeTextList) {
        this.exemptionTypeTextList = exemptionTypeTextList;
    }

}
