package com.northgateps.nds.beis.api.getconstrainedvalues;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
/**
 * The NDS request to register a new user 
 */
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to get lists of constrained values
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetConstrainedValuesNdsRequest", propOrder = { "requestedListNames" })
@XmlRootElement(name = "GetConstrainedValuesNdsRequest")
public class GetConstrainedValuesNdsRequest implements NdsRequest {

    private ArrayList<String> requestedListNames = new ArrayList<String>();

    public ArrayList<String> getRequestedListNames() {
        return requestedListNames;
    }

    public void setRequestedListNames(ArrayList<String> requestedListNames) {
        this.requestedListNames = requestedListNames;
    }

}
