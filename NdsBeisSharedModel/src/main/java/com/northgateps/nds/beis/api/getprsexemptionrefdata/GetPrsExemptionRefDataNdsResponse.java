package com.northgateps.nds.beis.api.getprsexemptionrefdata;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.platform.api.NdsErrorResponse;

/**
 * Response to hold exemption type text from the back office
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPrsExemptionRefDataNdsResponse", propOrder = { "exemptionTypeTextList" })
@XmlRootElement(name = "GetPrsExemptionRefDataNdsResponse")
public class GetPrsExemptionRefDataNdsResponse extends NdsErrorResponse{
    List<ExemptionTypeDetails> exemptionTypeTextList;

    public List<ExemptionTypeDetails> getExemptionTypeTextList() {
        return exemptionTypeTextList;
    }

    public void setExemptionTypeTextList(List<ExemptionTypeDetails> exemptionTypeTextList) {
        this.exemptionTypeTextList = exemptionTypeTextList;
    }
}
