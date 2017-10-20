package com.northgateps.nds.beis.api;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Class to contain all the information for a penalty response , that is returned from the server
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPenaltySearchResponseDetail", propOrder = { "penalties" })
public class GetPenaltySearchResponseDetail implements Serializable {

    private List<PenaltyData> penalties;

    public List<PenaltyData> getPenalties() {
        return penalties;
    }

    public void setPenalties(List<PenaltyData> penalties) {
        this.penalties = penalties;
    }
}
