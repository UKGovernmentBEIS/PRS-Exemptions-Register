package com.northgateps.nds.beis.api.getprspenaltyrefdata;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.beis.api.PenaltyTypeDetails;
import com.northgateps.nds.platform.api.NdsErrorResponse;

/**
 * Response to hold exemption type text from the back office
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPrsPenaltyRefDataNdsResponse", propOrder = { "penaltyTypeTextList" })
@XmlRootElement(name = "GetPrsPenaltyRefDataNdsResponse")
public class GetPrsPenaltyRefDataNdsResponse extends NdsErrorResponse{

    List<PenaltyTypeDetails> penaltyTypeTextList;

    
    public List<PenaltyTypeDetails> getPenaltyTypeTextList() {
        return penaltyTypeTextList;
    }

    
    public void setPenaltyTypeTextList(List<PenaltyTypeDetails> penaltyTypeTextList) {
        this.penaltyTypeTextList = penaltyTypeTextList;
    }
}
