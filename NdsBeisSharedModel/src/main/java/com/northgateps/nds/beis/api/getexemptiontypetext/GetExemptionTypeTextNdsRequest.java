package com.northgateps.nds.beis.api.getexemptiontypetext;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to get exemption type text
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetExemptionTypeTextNdsRequest", propOrder = { "filterCriteria" })
@XmlRootElement(name = "GetExemptionTypeTextNdsRequest")
public class GetExemptionTypeTextNdsRequest implements NdsRequest {

    private ArrayList<String> filterCriteria = new ArrayList<String>();

    public ArrayList<String> getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(ArrayList<String> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }
}
