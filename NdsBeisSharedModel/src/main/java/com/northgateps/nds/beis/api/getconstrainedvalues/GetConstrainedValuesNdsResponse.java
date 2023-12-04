package com.northgateps.nds.beis.api.getconstrainedvalues;

import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsErrorResponse;
import com.northgateps.nds.platform.api.ConstrainedValue;

/**
 * Response to get lists of constrained values
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetConstrainedValuesNdsResponse", propOrder = { "constrainedValues" })
@XmlRootElement(name = "GetConstrainedValuesNdsResponse")
public class GetConstrainedValuesNdsResponse extends NdsErrorResponse {

    /**
     * Contains each listName with a list of constrained values against it -
     * Outer String is the listName and inner String is the code
     */
    private TreeMap<String, ConstrainedValue[]> constrainedValues = new TreeMap<String, ConstrainedValue[]>();

    public TreeMap<String, ConstrainedValue[]> getConstrainedValues() {
        return constrainedValues;
    }

    public void setConstrainedValues(TreeMap<String, ConstrainedValue[]> constrainedValues) {
        this.constrainedValues = constrainedValues;
    }

}